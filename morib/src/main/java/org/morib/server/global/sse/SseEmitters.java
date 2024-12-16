package org.morib.server.global.sse;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
@Slf4j
public class SseEmitters {

    private final ConcurrentHashMap<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    SseEmitter add(Long userId, SseEmitter emitter) {
        this.emitters.put(userId, emitter);
        log.info("new emitter added: {}", emitter);
        log.info("emitter list size: {}", emitters.size());
        log.info("emitter list: {}", emitters);
        emitter.onCompletion(() -> {
            log.info("onCompletion callback");
            this.emitters.remove(userId);
        });
        emitter.onTimeout(() -> {
            log.info("onTimeout callback");
            emitter.complete();
        });

        return emitter;
    }

    public void pushNotifications(String fromName, Long toId, String msg) {
        SseEmitter emitter = this.emitters.get(toId);

        if (emitter != null) {
            try {
                emitter.send(
                        SseEmitter.event().name("friendRequest").data(fromName + msg));
            }catch (IOException e) {
                emitters.remove(toId); // 에러가 발생하면 emitter를 삭제
            }
        }
    }
}
