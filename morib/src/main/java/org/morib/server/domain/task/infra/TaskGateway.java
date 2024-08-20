package org.morib.server.domain.task.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskGateway {
    private final TaskRepository taskRepository;

    public List<Task> fetchTasksInRange() {
        // taskRepository에서 구간에 맞는 태스크를 조회 후 리턴
        return null;
    }
}
