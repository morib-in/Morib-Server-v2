package org.morib.server.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.timer.TimerManager;
import org.morib.server.domain.timer.application.FetchTimerService;
import org.morib.server.domain.timer.application.TimerSession.FetchTimerSessionService;
import org.morib.server.domain.timer.infra.Timer;
import org.morib.server.domain.timer.infra.TimerSession;
import org.morib.server.domain.timer.infra.TimerSessionRepository;
import org.morib.server.domain.timer.infra.TimerStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TimerScheduler {

    private final TimerSessionRepository timerSessionRepository;
    private final FetchTimerService fetchTimerService;
    private final TimerManager timerManager;
    private final FetchTimerSessionService fetchTimerSessionService;

    private static final Duration INACTIVITY_THRESHOLD = Duration.ofMinutes(2);

    @Scheduled(fixedRate = 60_000L)
    @Transactional
    public void updateActiveAndPauseInactiveTimers() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = LocalDate.now();
        LocalDateTime cutoffTime = now.minus(INACTIVITY_THRESHOLD);

        List<TimerSession> runningSessions = timerSessionRepository.findByTimerStatus(TimerStatus.RUNNING);

        if (runningSessions.isEmpty()) return;

        int updatedCount = 0;
        int pausedCount = 0;

        for (TimerSession session : runningSessions) {
            try {
                LocalDateTime lastCalculatedAt = session.getLastCalculatedAt();
                LocalDateTime lastHeartbeatTime = session.getLastHeartbeatAt();

                if (lastCalculatedAt == null) {
                    log.error("[타이머 스케줄러] 오류: 실행 중인 세션 {}의 lastCalculatedAt(lastStartedAt)이 null입니다. PAUSED 처리합니다.", session.getId());
                    session.setTimerStatus(TimerStatus.PAUSED);
                    session.setLastHeartbeatAt(null);
                    pausedCount++;
                    continue;
                }

                Duration elapsedDuration = Duration.ZERO;

                if (lastHeartbeatTime != null) {
                    elapsedDuration = Duration.between(lastCalculatedAt, now);
                } else {
                    log.warn("[타이머 스케줄러] 비활성 세션 {}의 lastHeartbeatTime({})이 유효하지 않아 경과 시간 0으로 처리.", session.getId(), lastHeartbeatTime);
                }
                int deltaSeconds = (int) Math.max(0, elapsedDuration.toSeconds());

                if (lastHeartbeatTime == null || lastHeartbeatTime.isBefore(cutoffTime)) {
                    // 비활성 세션의 경우, 마지막 계산 시점과 현재 시점 사이의 경과 시간을 계산
                    updateElapsedTimeAndPause(session, deltaSeconds);
                    pausedCount++;
                } else {
                    // 활성 세션의 경우, 마지막 계산 시점과 현재 시점 사이의 경과 시간을 계산
                    updateElapsedTimeAndContinue(session, deltaSeconds, now, today);
                    updatedCount++;
                }

            } catch (Exception e) {
                log.error("[타이머 스케줄러] 세션 {} 처리 중 오류 발생: {}", session.getId(), e.getMessage(), e);
            }
        }
        log.info("[타이머 스케줄러] 처리 완료. 활성 업데이트: {}건, 비활성 PAUSED: {}건", updatedCount, pausedCount);
    }

    // 비활성 세션의 경우 경과 시간을 업데이트하고 PAUSED 상태로 변경, 마지막 계산 시점과 마지막 하트비트 시점을 null로 설정
    private void updateElapsedTimeAndPause(TimerSession session, int deltaSeconds) {
        session.setElapsedTime(session.getElapsedTime() + deltaSeconds);
        session.setTimerStatus(TimerStatus.PAUSED);
        session.setLastCalculatedAt(null);
        session.setLastHeartbeatAt(null);

        updateTimerEntity(session.getSelectedTask(), session.getTargetDate(), deltaSeconds);

        log.info("[타이머 스케줄러] 세션 {} PAUSED 처리 완료. 최종 경과 시간: {}", session.getId(), session.getElapsedTime());
    }

    // 활성 세션의 경우 경과 시간을 업데이트하고 RUNNING 상태로 유지, 마지막 계산 시점을 현재 시점으로 설정
    private void updateElapsedTimeAndContinue(TimerSession session, int deltaSeconds, LocalDateTime calculationTime, LocalDate today) {
        session.setElapsedTime(session.getElapsedTime() + deltaSeconds);
        session.setLastCalculatedAt(calculationTime);

        updateTimerEntity(session.getSelectedTask(), today, deltaSeconds);

        log.info("[타이머 스케줄러] 세션 {} 시간 업데이트 완료. 누적 경과 시간: {}, 다음 계산 시작점: {}",
                 session.getId(), session.getElapsedTime(), session.getLastCalculatedAt());
    }

    private void updateTimerEntity(Task task, LocalDate targetDate, int deltaSeconds) {
        if (task == null) {
            log.error("[타이머 스케줄러] Timer 업데이트 실패: 세션에 선택된 태스크가 없습니다.");
            return;
        }
        if (deltaSeconds <= 0) {
             log.info("[타이머 스케줄러] deltaSeconds가 0 이하이므로 Timer 업데이트를 건너뛰었습니다. Task: {}", task.getId());
            return;
        }

        Timer timer = fetchTimerService.fetchByTaskIdAndTargetDate(task.getId(), targetDate);
        if (timer != null) {
            timerManager.addElapsedTime(timer, deltaSeconds);
            log.info("[타이머 스케줄러] Task {}의 Timer {}에 {}초 추가됨. 최종 시간: {}", task.getId(), timer.getId(), deltaSeconds, timer.getElapsedTime());
        } else {
            log.error("[타이머 스케줄러] Timer 업데이트 실패: Task ID {} 날짜 {}에 해당하는 Timer 엔티티를 찾을 수 없습니다.", task.getId(), targetDate);
        }
    }
}
