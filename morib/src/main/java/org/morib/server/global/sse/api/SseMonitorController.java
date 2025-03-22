package org.morib.server.global.sse.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.morib.server.global.sse.application.repository.SseMemoryMonitor;
import org.morib.server.global.sse.application.repository.SseRepository;
import org.morib.server.global.sse.application.repository.SseUserInfoWrapper;
import org.morib.server.global.sse.application.service.SseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v2/admin")
public class SseMonitorController {

    private final SseService sseService;
    private final SseRepository sseRepository;

    @GetMapping("/sse/monitor")
    public ResponseEntity<Map<String, Object>> getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        int connectionCount = sseService.getConnectionCount();
        
        Map<String, Object> result = new HashMap<>();
        result.put("totalMemoryMB", totalMemory / (1024 * 1024));
        result.put("freeMemoryMB", freeMemory / (1024 * 1024));
        result.put("usedMemoryMB", usedMemory / (1024 * 1024));
        result.put("sseConnectionCount", connectionCount);
        
        if (connectionCount > 0) {
            result.put("memoryPerConnectionKB", (usedMemory / connectionCount) / 1024);
        } else {
            result.put("memoryPerConnectionKB", 0);
        }
        // 연결된 사용자 ID 목록
        result.put("connectedUserIds", sseService.getConnectedUserIds());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/sse/gc")
    public ResponseEntity<Map<String, Object>> forceGarbageCollection() {
        Runtime runtime = Runtime.getRuntime();
        
        // 가비지 컬렉션 전 메모리 사용량
        long beforeGcMemory = runtime.totalMemory() - runtime.freeMemory();
        
        // 가비지 컬렉션 실행
        System.gc();
        
        // 가비지 컬렉션 후 메모리 사용량
        long afterGcMemory = runtime.totalMemory() - runtime.freeMemory();
        
        Map<String, Object> result = new HashMap<>();
        result.put("beforeGcMemoryMB", beforeGcMemory / (1024 * 1024));
        result.put("afterGcMemoryMB", afterGcMemory / (1024 * 1024));
        result.put("freedMemoryMB", (beforeGcMemory - afterGcMemory) / (1024 * 1024));
        result.put("sseConnectionCount", sseService.getConnectionCount());
        
        return ResponseEntity.ok(result);
    }

    @GetMapping("/sse/userInfo")
    public ResponseEntity<Map<String, Object>> getUserInfo() {
        Map<String, Object> result = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        // 연결 수와 총괄 정보
        int connectionCount = sseService.getConnectionCount();
        result.put("totalConnections", connectionCount);
        result.put("timestamp", new Date().toString());
        
        // 사용자별 상세 정보
        List<Map<String, Object>> userDetailsList = new ArrayList<>();
        
        Set<Long> connectedUserIds = sseService.getConnectedUserIds();
        for (Long userId : connectedUserIds) {
            Map<String, Object> userDetails = new HashMap<>();
            userDetails.put("userId", userId);
            
            // SseEmitter 정보
            SseEmitter emitter = sseRepository.getSseEmitterById(userId);
            if (emitter != null) {
                userDetails.put("emitterHashCode", emitter.hashCode());
                userDetails.put("emitterClass", emitter.getClass().getName());
            } else {
                userDetails.put("emitterStatus", "NULL");
            }
            
            // SseUserInfoWrapper 정보
            SseUserInfoWrapper userInfo = sseRepository.getSseUserInfoWrapperById(userId);
            if (userInfo != null) {
                Map<String, Object> userInfoMap = new HashMap<>();
                userInfoMap.put("elapsedTime", userInfo.getElapsedTime());
                userInfoMap.put("runningCategoryName", userInfo.getRunningCategoryName());
                userInfoMap.put("taskId", userInfo.getTaskId());
                userInfoMap.put("timerStatus", userInfo.getTimerStatus() != null ? userInfo.getTimerStatus().name() : "NULL");
                
                if (userInfo.getLastTimerStatusChangeTime() != null) {
                    userInfoMap.put("lastTimerStatusChangeTime", userInfo.getLastTimerStatusChangeTime().format(formatter));
                } else {
                    userInfoMap.put("lastTimerStatusChangeTime", "NULL");
                }
                
                userDetails.put("userInfo", userInfoMap);
            } else {
                userDetails.put("userInfo", "NULL");
            }
            
            userDetailsList.add(userDetails);
        }
        
        // 타이머 상태별 통계
        Map<String, Long> timerStatusStats = new HashMap<>();
        long runningCount = userDetailsList.stream()
                .filter(details -> {
                    Object userInfoObj = details.get("userInfo");
                    if (!(userInfoObj instanceof Map)) {
                        return false;
                    }
                    Map<String, Object> userInfo = (Map<String, Object>) userInfoObj;
                    return "RUNNING".equals(userInfo.get("timerStatus"));
                })
                .count();
        
        long pausedCount = userDetailsList.stream()
                .filter(details -> {
                    Object userInfoObj = details.get("userInfo");
                    if (!(userInfoObj instanceof Map)) {
                        return false;
                    }
                    Map<String, Object> userInfo = (Map<String, Object>) userInfoObj;
                    return "PAUSED".equals(userInfo.get("timerStatus"));
                })
                .count();
        
        timerStatusStats.put("RUNNING", runningCount);
        timerStatusStats.put("PAUSED", pausedCount);
        timerStatusStats.put("NULL_OR_UNKNOWN", connectionCount - runningCount - pausedCount);
        
        result.put("userDetails", userDetailsList);
        result.put("timerStatusStats", timerStatusStats);
        
        // 메모리 정보 추가
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        
        Map<String, Object> memoryInfo = new HashMap<>();
        memoryInfo.put("totalMemoryMB", totalMemory / (1024 * 1024));
        memoryInfo.put("freeMemoryMB", freeMemory / (1024 * 1024));
        memoryInfo.put("usedMemoryMB", usedMemory / (1024 * 1024));
        
        if (connectionCount > 0) {
            memoryInfo.put("averageMemoryPerConnectionKB", (usedMemory / connectionCount) / 1024);
        } else {
            memoryInfo.put("averageMemoryPerConnectionKB", 0);
        }
        
        result.put("memoryInfo", memoryInfo);
        
        return ResponseEntity.ok(result);
    }
} 