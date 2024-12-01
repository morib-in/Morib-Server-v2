package org.morib.server.domain.task.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.task.infra.TaskRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteTaskServiceImpl implements
    DeleteTaskService {

    private final TaskRepository taskRepository;


    @Override
    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }
}
