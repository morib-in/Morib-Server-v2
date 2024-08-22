package org.morib.server.domain.timer;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.timer.infra.Timer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TimerManager {

    public void aggregate() {
        // 각 task의 timer들을 종합해 오늘 나의 작업시간 계산
    }
}
