package org.morib.server.facade; // 실제 프로젝트 경로로 수정

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.morib.server.api.homeView.dto.StartTimerRequestDto;
import org.morib.server.api.homeView.facade.HomeViewFacade;
import org.morib.server.api.timerView.dto.TimerDtos;
import org.morib.server.api.timerView.facade.TimerViewFacade;
import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.category.infra.CategoryRepository;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.task.infra.TaskRepository;
import org.morib.server.domain.timer.TimerManager;
import org.morib.server.domain.timer.application.FetchTimerService;
import org.morib.server.domain.timer.infra.*;
import org.morib.server.domain.user.infra.User;
import org.morib.server.domain.user.infra.UserRepository;
import org.morib.server.domain.user.infra.type.Platform;
import org.morib.server.scheduler.TimerScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TimerFullScenarioIntegrationTest {

    // --- 의존성 주입 (이전과 동일) ---
    @Autowired private HomeViewFacade homeViewFacade;
    @Autowired private TimerViewFacade timerViewFacade;
    @Autowired private TimerScheduler timerScheduler;
    @Autowired private UserRepository userRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private TaskRepository taskRepository;
    @Autowired private TimerSessionRepository timerSessionRepository;
    @Autowired private TimerRepository timerRepository;
    @Autowired private FetchTimerService fetchTimerService;
    @Autowired private EntityManager em;

    // --- 테스트 데이터 (이전과 동일) ---
    private User testUser;
    private Category testCategory;
    private Task task1, task2, task3;
    private LocalDate targetDate;

    @BeforeEach
    void setUp() {
        // ... (이전과 동일한 데이터 설정) ...
        targetDate = LocalDate.now();
        testUser = userRepository.save(User.builder().email("full@test.com").name("fulltester").platform(Platform.GOOGLE).isPushEnabled(false).isOnboardingCompleted(true).build());
        testCategory = categoryRepository.save(Category.builder().user(testUser).name("Full Test Category").build());
        task1 = taskRepository.save(Task.builder().category(testCategory).name("Task 1").startDate(targetDate).isComplete(false).build());
        task2 = taskRepository.save(Task.builder().category(testCategory).name("Task 2").startDate(targetDate).isComplete(false).build());
        task3 = taskRepository.save(Task.builder().category(testCategory).name("Inactive Task For Scheduler").startDate(targetDate).isComplete(false).build());

        timerRepository.save(Timer.builder().user(testUser).task(task1).targetDate(targetDate).elapsedTime(0).build());
        timerRepository.save(Timer.builder().user(testUser).task(task2).targetDate(targetDate).elapsedTime(0).build());
        timerRepository.save(Timer.builder().user(testUser).task(task3).targetDate(targetDate).elapsedTime(0).build());

        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("타이머 전체 시나리오 (진입, 시작, 하트비트, 선택, 동기화/조회, 시작, 정지, 스케줄러) 통합 테스트")
    void timerFullScenarioTest() throws InterruptedException {
        Long userId = testUser.getId();
        LocalDateTime testStartTime = LocalDateTime.now();
        targetDate = LocalDate.now();
        // --- 시나리오 시작 ---

        // [단계 0] 타이머 진입
        System.out.println("\n[단계 0] 타이머 진입 (Task 1, 2, 3)");
        // ... (enterTimer 호출 및 검증 0 - 이전과 동일) ...
        List<Long> taskIdsToEnter = List.of(task1.getId(), task2.getId(), task3.getId());
        StartTimerRequestDto enterRequest = new StartTimerRequestDto(taskIdsToEnter);
        homeViewFacade.enterTimer(userId, enterRequest, targetDate);
        TimerSession sessionAfterEnter = findTimerSession(userId, targetDate);
        assertTimerSessionState(sessionAfterEnter, TimerStatus.PAUSED, task1.getId(), 0, "진입 후");

        TimerDtos.TimerRequest selectRequest1 = new TimerDtos.TimerRequest(task1.getId(), targetDate);
        timerViewFacade.selectTimerInfo(userId, selectRequest1); // Task 1 선택

        TimerSession sessionAfterSelect1 = findTimerSession(userId, targetDate);
        assertTimerSessionState(sessionAfterSelect1, TimerStatus.PAUSED, task1.getId(), 0, "Task 1 선택 후");
        assertThat(sessionAfterSelect1.getLastCalculatedAt()).isNull(); // Pause 상태이므로 null
        assertThat(sessionAfterSelect1.getLastHeartbeatAt()).isNull(); // Pause 상태이므로 null


        // [단계 1] Task 1 시작
        System.out.println("\n[단계 1] Task 1 시작");
        // ... (runTimer 호출 및 검증 1 - 이전과 동일) ...
        TimerDtos.TimerRequest runRequest1 = new TimerDtos.TimerRequest(task1.getId(), targetDate);
        timerViewFacade.runTimer(userId, runRequest1);
        LocalDateTime run1Time = LocalDateTime.now(); // 시작 시각 기록
        TimerSession sessionAfterRun1 = findTimerSession(userId, targetDate);
        assertTimerSessionState(sessionAfterRun1, TimerStatus.RUNNING, task1.getId(), 0, "Task 1 시작 후");

        // [단계 2] Task 1 실행 중 - 하트비트
        System.out.println("\n[단계 2] Task 1 실행 중 - 1초 후 하트비트");
        // ... (handleHeartbeat 호출 및 검증 2 - 이전과 동일) ...
        sleepSeconds(1); // 1초 대기
        LocalDateTime heartbeat1Time = LocalDateTime.now();
        timerViewFacade.handleHeartbeat(userId, targetDate);
        TimerSession sessionAfterHeartbeat1 = findTimerSession(userId, targetDate);
        assertTimerSessionState(sessionAfterHeartbeat1, TimerStatus.RUNNING, task1.getId(), 0, "하트비트 1 후"); // 시간은 아직 0
        assertThat((int) Duration.between(sessionAfterHeartbeat1.getLastHeartbeatAt(), heartbeat1Time).toSeconds() < 1);


        // [단계 3] Task 1 정지 (이전 단계 3에서 이동)
        System.out.println("\n[단계 3] Task 1 실행 중 - 추가 1초 후 정지");
        sleepSeconds(1); // 추가 1초 대기
        LocalDateTime pause1Time = LocalDateTime.now();
        TimerDtos.TimerRequest pauseRequest1 = new TimerDtos.TimerRequest(task1.getId(), targetDate);
        timerViewFacade.pauseTimer(userId, pauseRequest1);

        // 검증 3: Task 1 정지 후 상태
        int deltaT1 = (int) Duration.between(run1Time, pause1Time).toSeconds(); // Task 1 실행 시간 (약 2초)
        int expectedElapsedAfterPause1 = 0 + deltaT1;
        TimerSession sessionAfterPause1 = findTimerSession(userId, targetDate);
        assertTimerSessionState(sessionAfterPause1, TimerStatus.PAUSED, task1.getId(), expectedElapsedAfterPause1, "Task 1 정지 후");
        assertTimerState(task1.getId(), targetDate, expectedElapsedAfterPause1, "Task 1 정지 후");


        // --- 새로운 플로우 시작 ---

        // [단계 4] Task 2 선택 (/timer/select)
        System.out.println("\n[단계 4] Task 2 선택");
        TimerDtos.TimerRequest selectRequest2 = new TimerDtos.TimerRequest(task2.getId(), targetDate);
        timerViewFacade.selectTimerInfo(userId, selectRequest2); // Task 2 선택

        // 검증 4: Task 2 선택 후 상태
        TimerSession sessionAfterSelect2 = findTimerSession(userId, targetDate);
        // selectTimerInfo는 선택된 Task만 바꾸고, 상태는 PAUSED, 시간은 *이전 Task의 최종 시간*을 유지한다고 가정
        // (만약 Task 2 시간(0)으로 리셋한다면 expectedElapsedAfterPause1 -> 0 으로 변경 필요)
        assertTimerSessionState(sessionAfterSelect2, TimerStatus.PAUSED, task2.getId(), 0, "Task 2 선택 후");
        assertThat(sessionAfterSelect2.getLastCalculatedAt()).isNull(); // Pause 상태이므로 null
        assertThat(sessionAfterSelect2.getLastHeartbeatAt()).isNull(); // Pause 상태이므로 null


        // [단계 5] Task 2 조회 및 동기화 (/timer/sync)
        System.out.println("\n[단계 5] Task 2 조회 및 동기화 (현재 PAUSED 상태)");
        LocalDateTime sync2Time = LocalDateTime.now();
        // PAUSED 상태에서 sync 호출 시, 시간 계산 없이 현재 상태만 반환해야 함
        // lastSyncClientTime은 의미 없지만 형식상 전달 (예: 이전 pause 시간)

        // [단계 6] Task 2 시작 (/timer/run)
        System.out.println("\n[단계 6] Task 2 시작");
        TimerDtos.TimerRequest runRequest2 = new TimerDtos.TimerRequest(task2.getId(), targetDate);
        // runTimer는 선택된 Task (Task 2)의 Timer에서 elapsedTime(현재 0)을 가져와 세션에 설정해야 함
        timerViewFacade.runTimer(userId, runRequest2);
        LocalDateTime run2Time = LocalDateTime.now();

        // 검증 6: Task 2 시작 후 상태
        TimerSession sessionAfterRun2 = findTimerSession(userId, targetDate);
        assertTimerSessionState(sessionAfterRun2, TimerStatus.RUNNING, task2.getId(), 0, "Task 2 시작 후"); // Task 2 시간(0)으로 리셋됨
        assertThat((int) Duration.between(sessionAfterRun2.getLastHeartbeatAt(), run2Time).toSeconds() < 1);
        assertThat((int) Duration.between(sessionAfterRun2.getLastHeartbeatAt(), run2Time).toSeconds() < 1);

        sleepSeconds(2);
        TimerDtos.TimerStatusResponse syncResponse2 = timerViewFacade.getSelectedTimerInfo(userId, targetDate);

        // 검증 5: 동기화(조회) 후 상태 (변경 없어야 함)
        assertThat(syncResponse2.timerStatus()).isEqualTo(TimerStatus.RUNNING);
        assertThat(syncResponse2.selectedTaskId()).isEqualTo(task2.getId());
        assertThat(syncResponse2.elapsedTime()).isEqualTo(2); // 이전 시간 그대로

        TimerSession sessionAfterSync2 = findTimerSession(userId, targetDate);
        assertTimerSessionState(sessionAfterSync2, TimerStatus.RUNNING, task2.getId(), 2, "Task 2 조회/동기화 후");



        // [단계 7] Task 2 실행 중 - 정지 (/timer/pause)
//        System.out.println("\n[단계 7] Task 2 실행 중 - 2초 후 정지");
//        sleepSeconds(2); // 2초 대기
//        LocalDateTime pause2Time = LocalDateTime.now();
//        TimerDtos.TimerRequest pauseRequest2 = new TimerDtos.TimerRequest(task2.getId(), targetDate);
//        timerViewFacade.pauseTimer(userId, pauseRequest2);
//
//        // 검증 7: Task 2 정지 후 상태
//        int deltaT2 = (int) Duration.between(run2Time, pause2Time).toSeconds(); // Task 2 실행 시간 (약 2초)
//        int expectedElapsedAfterPause2 = 0 + deltaT2; // Task 2 누적 시간
//        TimerSession sessionAfterPause2 = findTimerSession(userId, targetDate);
//        assertTimerSessionState(sessionAfterPause2, TimerStatus.PAUSED, task2.getId(), expectedElapsedAfterPause2, "Task 2 정지 후");
//        assertTimerState(task1.getId(), targetDate, expectedElapsedAfterPause1, "Task 2 정지 후 Task 1"); // Task 1 시간 유지
//        assertTimerState(task2.getId(), targetDate, expectedElapsedAfterPause2, "Task 2 정지 후 Task 2"); // Task 2 최종 시간


        // [단계 8] 스케줄러 테스트 (이전 단계 7과 동일)
        System.out.println("\n[단계 8] 비활성 세션(Task 3) 생성 및 스케줄러 테스트");
        // ... (스케줄러 테스트 로직 - 이전과 동일) ...
        LocalDateTime inactiveStartTime = LocalDateTime.now();
        TimerDtos.TimerRequest selectRequest3 = new TimerDtos.TimerRequest(task3.getId(), targetDate);
        timerViewFacade.selectTimerInfo(userId, selectRequest3);
        TimerDtos.TimerRequest runRequest3 = new TimerDtos.TimerRequest(task3.getId(), targetDate);
        timerViewFacade.runTimer(userId, runRequest3);
        LocalDateTime run3Time = LocalDateTime.now();
        System.out.println(" - 1분 30초 대기...");
        sleepSeconds(90);
        System.out.println(" - 스케줄러 실행 (활성 시간 업데이트)");
        timerScheduler.updateActiveAndPauseInactiveTimers();
        LocalDateTime schedulerRun1Time = LocalDateTime.now();
        int deltaScheduler1 = (int) Duration.between(run3Time, schedulerRun1Time).toSeconds();
        TimerSession sessionAfterScheduler1 = findTimerSession(userId, targetDate);
        assertTimerSessionState(sessionAfterScheduler1, TimerStatus.RUNNING, task3.getId(), 0 + deltaScheduler1, "스케줄러 1차 실행 후");
        assertTimerState(task3.getId(), targetDate, deltaScheduler1, "스케줄러 1차 실행 후 Task 3");
        System.out.println(" - 추가 1분 대기...");
        sleepSeconds(60);
        System.out.println(" - 스케줄러 실행 (비활성 감지 및 PAUSE)");
        timerScheduler.updateActiveAndPauseInactiveTimers();
        LocalDateTime schedulerRun2Time = LocalDateTime.now();
        int deltaScheduler2 = (int) Duration.between(schedulerRun1Time, schedulerRun2Time).toSeconds(); // heartbeat 없었으므로 delta 0 가정
        int finalSchedulerElapsed = deltaScheduler1 + deltaScheduler2;
        TimerSession sessionAfterScheduler2 = findTimerSession(userId, targetDate);
        assertTimerSessionState(sessionAfterScheduler2, TimerStatus.PAUSED, task3.getId(), finalSchedulerElapsed, "스케줄러 2차 실행 후");
        assertTimerState(task3.getId(), targetDate, finalSchedulerElapsed, "스케줄러 2차 실행 후 Task 3");


        System.out.println("\n--- 시나리오 종료 ---");
    }


    // --- Helper Methods (이전과 동일) ---
    private TimerSession findTimerSession(Long userId, LocalDate date) {
        return timerSessionRepository.findByUserIdAndTargetDate(userId, date);
    }

    private Timer findTimer(Long taskId, LocalDate date) {
        Timer timer = fetchTimerService.fetchByTaskIdAndTargetDate(taskId, date);
        assertNotNull(timer, "Timer를 찾을 수 없습니다. TaskId: " + taskId + ", Date: " + date);
        return timer;
    }

    private void assertTimerSessionState(TimerSession session, TimerStatus expectedStatus, Long expectedTaskId, int expectedElapsedTime, String context) {
        System.out.println(" [" + context + "] 세션 검증: ID=" + session.getId() + ", 상태=" + session.getTimerStatus() + ", 선택Task=" + (session.getSelectedTask() != null ? session.getSelectedTask().getId() : "null") + ", 경과시간=" + session.getElapsedTime());
        assertThat(session.getTimerStatus()).as("[" + context + "] 타이머 상태").isEqualTo(expectedStatus);
        if (expectedTaskId != null) {
            assertThat(session.getSelectedTask()).as("[" + context + "] 선택된 태스크").isNotNull();
            assertThat(session.getSelectedTask().getId()).as("[" + context + "] 선택된 태스크 ID").isEqualTo(expectedTaskId);
        } else {
            assertThat(session.getSelectedTask()).as("[" + context + "] 선택된 태스크").isNull();
        }
        assertThat(session.getElapsedTime()).as("[" + context + "] 세션 경과 시간").isEqualTo(expectedElapsedTime);
    }

    private void assertTimerState(Long taskId, LocalDate date, int expectedElapsedTime, String context) {
        Timer timer = findTimer(taskId, date);
        System.out.println(" [" + context + "] Timer 검증: TaskID=" + taskId + ", 경과시간=" + timer.getElapsedTime());
        assertThat(timer.getElapsedTime()).as("[" + context + "] Timer 경과 시간 (Task: " + taskId + ")").isEqualTo(expectedElapsedTime);
    }

    private void sleepSeconds(int seconds) throws InterruptedException {
        Thread.sleep(seconds * 1000L);
    }
}