package org.morib.server.domain.task.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.task.infra.TaskRepository;
import org.morib.server.global.exception.NotFoundException;
import org.morib.server.global.message.ErrorMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteTaskServiceImpl implements
    DeleteTaskService {

    private final TaskRepository taskRepository;


    @Override
    public void deleteByTaskId(Long taskId) {
        taskRepository.findById(taskId).ifPresentOrElse(taskRepository::delete, () -> {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        });
    }
}
