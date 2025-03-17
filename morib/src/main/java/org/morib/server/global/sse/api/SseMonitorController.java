package org.morib.server.global.sse.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.morib.server.global.sse.application.repository.SseMemoryMonitor;
import org.morib.server.global.sse.application.repository.SseRepository;
import org.morib.server.global.sse.application.service.SseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v2/admin")
public class SseMonitorController {

    private final SseService sseService;

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
} 