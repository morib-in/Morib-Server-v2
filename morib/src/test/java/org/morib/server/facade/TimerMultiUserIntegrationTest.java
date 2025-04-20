package org.morib.server.facade;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.morib.server.api.homeView.dto.StartTimerRequestDto;
import org.morib.server.api.homeView.facade.HomeViewFacade;
import org.morib.server.api.timerView.dto.TimerRequestDto;
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
class TimerMultiUserIntegrationTest {

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

    // --- 테스트 데이터 ---
    private User testUser1, testUser2;
    private Category testCategory1, testCategory2;
    private Task task1_1, task1_2, task1_3; // User 1 tasks
    private Task task2_1, task2_2;      // User 2 tasks
    private LocalDate targetDate;

    @BeforeEach
    void setUp() {
        targetDate = LocalDate.now();

        // User 1 설정
        testUser1 = userRepository.save(User.builder().email("user1@test.com").name("tester1").platform(Platform.GOOGLE).build());
        testCategory1 = categoryRepository.save(Category.builder().user(testUser1).name("User1 Category").build());
        task1_1 = taskRepository.save(Task.builder().category(testCategory1).name("Task 1-1").startDate(targetDate).isComplete(false).build());
        task1_2 = taskRepository.save(Task.builder().category(testCategory1).name("Task 1-2").startDate(targetDate).isComplete(false).build());
        task1_3 = taskRepository.save(Task.builder().category(testCategory1).name("Task 1-3 (Scheduler)").startDate(targetDate).isComplete(false).build());
        timerRepository.save(Timer.builder().user(testUser1).task(task1_1).targetDate(targetDate).elapsedTime(0).build());
        timerRepository.save(Timer.builder().user(testUser1).task(task1_2).targetDate(targetDate).elapsedTime(0).build());
        timerRepository.save(Timer.builder().user(testUser1).task(task1_3).targetDate(targetDate).elapsedTime(0).build());

        // User 2 설정
        testUser2 = userRepository.save(User.builder().email("user2@test.com").name("tester2").platform(Platform.GOOGLE).build());
        testCategory2 = categoryRepository.save(Category.builder().user(testUser2).name("User2 Category").build());
        task2_1 = taskRepository.save(Task.builder().category(testCategory2).name("Task 2-1").startDate(targetDate).isComplete(false).build());
        task2_2 = taskRepository.save(Task.builder().category(testCategory2).name("Task 2-2 (Scheduler)").startDate(targetDate).isComplete(false).build());
        timerRepository.save(Timer.builder().user(testUser2).task(task2_1).targetDate(targetDate).elapsedTime(0).build());
        timerRepository.save(Timer.builder().user(testUser2).task(task2_2).targetDate(targetDate).elapsedTime(0).build());

        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("여러 사용자 타이머 시나리오 통합 테스트")
    void multiUserTimerScenarioTest() throws InterruptedException {
        Long userId1 = testUser1.getId();
        Long userId2 = testUser2.getId();
        targetDate = LocalDate.now(); // 필요 시 재설정

        // --- 시나리오 시작 ---

        // [단계 0] 각 사용자 타이머 진입
        System.out.println("\n[단계 0] 사용자 1, 2 타이머 진입");
        homeViewFacade.enterTimer(userId1, new StartTimerRequestDto(List.of(task1_1.getId(), task1_2.getId(), task1_3.getId())), targetDate);
        homeViewFacade.enterTimer(userId2, new StartTimerRequestDto(List.of(task2_1.getId(), task2_2.getId())), targetDate);

        TimerSession session1AfterEnter = findTimerSession(userId1, targetDate);
        TimerSession session2AfterEnter = findTimerSession(userId2, targetDate);
        assertTimerSessionState(session1AfterEnter, TimerStatus.PAUSED, task1_1.getId(), 0, "User 1 진입 후");
        assertTimerSessionState(session2AfterEnter, TimerStatus.PAUSED, task2_1.getId(), 0, "User 2 진입 후");

        // [단계 1] 각 사용자 Task 시작
        System.out.println("\n[단계 1] 사용자 1 Task 1-1 시작, 사용자 2 Task 2-1 시작");
        timerViewFacade.runTimer(userId1, new TimerRequestDto(task1_1.getId(), targetDate));
        LocalDateTime run1_1Time = LocalDateTime.now();
        timerViewFacade.runTimer(userId2, new TimerRequestDto(task2_1.getId(), targetDate));
        LocalDateTime run2_1Time = LocalDateTime.now();

        assertTimerSessionState(findTimerSession(userId1, targetDate), TimerStatus.RUNNING, task1_1.getId(), 0, "User 1 시작 후");
        assertTimerSessionState(findTimerSession(userId2, targetDate), TimerStatus.RUNNING, task2_1.getId(), 0, "User 2 시작 후");

        // [단계 2] 사용자 1 하트비트, 사용자 2 정지
        System.out.println("\n[단계 2] 사용자 1 하트비트, 사용자 2 정지");
        sleepSeconds(1);
        timerViewFacade.handleHeartbeat(userId1, targetDate);
        LocalDateTime pause2_1Time = LocalDateTime.now();
        timerViewFacade.pauseTimer(userId2, new TimerRequestDto(task2_1.getId(), targetDate));

        int delta2_1 = (int) Duration.between(run2_1Time, pause2_1Time).toSeconds();
        assertTimerSessionState(findTimerSession(userId1, targetDate), TimerStatus.RUNNING, task1_1.getId(), 0, "User 1 하트비트 후"); // 시간은 스케줄러나 다음 액션에서 계산됨
        assertTimerSessionState(findTimerSession(userId2, targetDate), TimerStatus.PAUSED, task2_1.getId(), delta2_1, "User 2 정지 후");
        assertTimerState(task2_1.getId(), targetDate, delta2_1, "User 2 정지 후");


        // [단계 3] 사용자 1 정지, 사용자 2 Task 2-2 선택
        System.out.println("\n[단계 3] 사용자 1 정지, 사용자 2 Task 2-2 선택");
        sleepSeconds(1);
        LocalDateTime pause1_1Time = LocalDateTime.now();
        timerViewFacade.pauseTimer(userId1, new TimerRequestDto(task1_1.getId(), targetDate));
        timerViewFacade.selectTimerInfo(userId2, new TimerRequestDto(task2_2.getId(), targetDate));

        int delta1_1 = (int) Duration.between(run1_1Time, pause1_1Time).toSeconds(); // User 1의 Task 1-1 총 실행 시간 (약 2초)
        assertTimerSessionState(findTimerSession(userId1, targetDate), TimerStatus.PAUSED, task1_1.getId(), delta1_1, "User 1 정지 후");
        assertTimerState(task1_1.getId(), targetDate, delta1_1, "User 1 정지 후");
        assertTimerSessionState(findTimerSession(userId2, targetDate), TimerStatus.PAUSED, task2_2.getId(), 0, "User 2 Task 2-2 선택 후"); // Task 2-2 시간(0)으로 리셋 가정

        // [단계 4] 스케줄러 테스트 준비: 두 사용자 모두 다른 Task 실행
        System.out.println("\n[단계 4] 스케줄러 테스트 준비: User 1 Task 1-3 시작, User 2 Task 2-2 시작");
        timerViewFacade.selectTimerInfo(userId1, new TimerRequestDto(task1_3.getId(), targetDate)); // User1 Task 1-3 선택
        timerViewFacade.runTimer(userId1, new TimerRequestDto(task1_3.getId(), targetDate));       // User1 Task 1-3 시작
        LocalDateTime run1_3Time = LocalDateTime.now();
        // User 2는 이미 Task 2-2가 선택된 상태
        timerViewFacade.runTimer(userId2, new TimerRequestDto(task2_2.getId(), targetDate));       // User2 Task 2-2 시작
        LocalDateTime run2_2Time = LocalDateTime.now();

        assertTimerSessionState(findTimerSession(userId1, targetDate), TimerStatus.RUNNING, task1_3.getId(), 0, "User 1 Task 1-3 시작 후");
        assertTimerSessionState(findTimerSession(userId2, targetDate), TimerStatus.RUNNING, task2_2.getId(), 0, "User 2 Task 2-2 시작 후");

        // [단계 5] 스케줄러 테스트 실행
        System.out.println("\n[단계 5] 스케줄러 테스트 실행");
        System.out.println(" - 1분 30초 대기...");
        sleepSeconds(90); // 예시 시간, 실제 INACTIVITY_THRESHOLD 고려 필요

        System.out.println(" - 스케줄러 실행 (활성 시간 업데이트)");
        timerScheduler.updateActiveAndPauseInactiveTimers(); // 스케줄러 실행
        LocalDateTime schedulerRun1Time = LocalDateTime.now();

        // 검증 5-1: 스케줄러 1차 실행 후 (두 세션 모두 활성)
        int deltaSched1_User1 = (int) Duration.between(run1_3Time, schedulerRun1Time).toSeconds();
        int deltaSched1_User2 = (int) Duration.between(run2_2Time, schedulerRun1Time).toSeconds();

        TimerSession session1AfterSched1 = findTimerSession(userId1, targetDate);
        TimerSession session2AfterSched1 = findTimerSession(userId2, targetDate);
        assertTimerSessionState(session1AfterSched1, TimerStatus.RUNNING, task1_3.getId(), 0 + deltaSched1_User1, "User 1 스케줄러 1차 후");
        assertTimerState(task1_3.getId(), targetDate, deltaSched1_User1, "User 1 스케줄러 1차 후");
        assertTimerSessionState(session2AfterSched1, TimerStatus.RUNNING, task2_2.getId(), 0 + deltaSched1_User2, "User 2 스케줄러 1차 후");
        assertTimerState(task2_2.getId(), targetDate, deltaSched1_User2, "User 2 스케줄러 1차 후");


        System.out.println(" - 추가 1분 대기 (비활성 유도)...");
        sleepSeconds(60); // 비활성 유도 (INACTIVITY_THRESHOLD 초과 가정)

        System.out.println(" - 스케줄러 실행 (비활성 감지 및 PAUSE)");
        timerScheduler.updateActiveAndPauseInactiveTimers(); // 스케줄러 실행
        LocalDateTime schedulerRun2Time = LocalDateTime.now(); // 비활성 Pause 시점 계산 위해 사용

        // 검증 5-2: 스케줄러 2차 실행 후 (두 세션 모두 비활성 PAUSED)
        // 비활성 시 시간 계산 정책에 따라 deltaScheduler2 계산 (여기선 0으로 가정, 실제 구현 확인 필요)
        int finalSchedElapsed_User1 = (deltaSched1_User1) + (int) Duration.between(schedulerRun1Time, schedulerRun2Time).toSeconds();
        int finalSchedElapsed_User2 = (deltaSched1_User2) + (int) Duration.between(schedulerRun1Time, schedulerRun2Time).toSeconds();

        TimerSession session1AfterSched2 = findTimerSession(userId1, targetDate);
        TimerSession session2AfterSched2 = findTimerSession(userId2, targetDate);
        assertTimerSessionState(session1AfterSched2, TimerStatus.PAUSED, task1_3.getId(), finalSchedElapsed_User1, "User 1 스케줄러 2차 후");
        assertTimerState(task1_3.getId(), targetDate, finalSchedElapsed_User1, "User 1 스케줄러 2차 후");
        assertTimerSessionState(session2AfterSched2, TimerStatus.PAUSED, task2_2.getId(), finalSchedElapsed_User2, "User 2 스케줄러 2차 후");
        assertTimerState(task2_2.getId(), targetDate, finalSchedElapsed_User2, "User 2 스케줄러 2차 후");


        System.out.println("\n--- 시나리오 종료 ---");
    }


    // --- Helper Methods (이전과 동일, findTimerSession은 Optional 처리 권장) ---
    private TimerSession findTimerSession(Long userId, LocalDate date) {
        // Optional<TimerSession> sessionOpt = timerSessionRepository.findByUserIdAndTargetDate(userId, date);
        // assertTrue(sessionOpt.isPresent(), "TimerSession을 찾을 수 없습니다. UserId: " + userId + ", Date: " + date);
        // return sessionOpt.get();
        // Optional 처리를 하거나, 테스트 데이터 생성을 보장한다면 바로 get() 사용 가능
        return timerSessionRepository.findByUserIdAndTargetDate(userId, date);

    }

    private Timer findTimer(Long taskId, LocalDate date) {
        Timer timer = fetchTimerService.fetchByTaskIdAndTargetDate(taskId, date);
        assertNotNull(timer, "Timer를 찾을 수 없습니다. TaskId: " + taskId + ", Date: " + date);
        return timer;
    }

    private void assertTimerSessionState(TimerSession session, TimerStatus expectedStatus, Long expectedTaskId, int expectedElapsedTime, String context) {
        System.out.println(" [" + context + "] 세션 검증: UserID=" + session.getUserId() + ", SessionID=" + session.getId() + ", 상태=" + session.getTimerStatus() + ", 선택Task=" + (session.getSelectedTask() != null ? session.getSelectedTask().getId() : "null") + ", 경과시간=" + session.getElapsedTime());
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
