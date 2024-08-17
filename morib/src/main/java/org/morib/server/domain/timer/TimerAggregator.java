package org.morib.server.domain.timer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TimerAggregator {

    public void aggregate() {
        // 각 task의 timer들을 종합해 오늘 나의 작업시간 계산
    }
}
