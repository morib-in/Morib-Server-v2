package org.morib.server.global.sse.application.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * SSE 연결의 메모리 사용량을 모니터링하는 클래스
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SseMemoryMonitor {

    private final SseRepository sseRepository;

    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void logMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        int connectionCount = sseRepository.getConnectionCount();
        
        log.info("===== SSE Memory Monitor =====");
        log.info("Total memory: {} MB", runtime.totalMemory() / (1024 * 1024));
        log.info("Free memory: {} MB", runtime.freeMemory() / (1024 * 1024));
        log.info("Used memory: {} MB", usedMemory / (1024 * 1024));
        log.info("SSE connection count: {}", connectionCount);
        
        if (connectionCount > 0) {
            log.info("Estimated memory per connection: {} KB", (usedMemory / connectionCount) / 1024);
        }
        
        // 각 연결의 세부 정보 로깅
        SseRepository.emitters.forEach((userId, wrapper) -> {
            log.debug("Connection ID: {}, Category: {}, ElapsedTime: {}", 
                    userId, wrapper.getRunningCategoryName(), wrapper.getElapsedTime());
        });
    }
} 