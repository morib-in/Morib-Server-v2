package org.morib.server.domain.task.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.task.infra.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskGatewayImpl implements TaskGateway{
    private final TaskRepository taskRepository;

    @Override
    public void save() {

    }

    @Override
    public void findById() {

    }

    @Override
    public void findAll() {

    }

    @Override
    public void deleteById() {

    }

    @Override
    public void deleteAll() {

    }

    public List<Task> fetchTasksInRange() {
        // taskRepository에서 구간에 맞는 태스크를 조회 후 리턴
        return null;
    }
}
