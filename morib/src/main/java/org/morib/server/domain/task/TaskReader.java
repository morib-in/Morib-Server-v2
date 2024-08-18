package org.morib.server.domain.task;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.task.infra.TaskRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskReader {
    private final TaskRepository taskRepository;

    public void fetch() {
        // taskRepository에서 각 category의 task를 조회 + 구간
    }
}
