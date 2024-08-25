package org.morib.server.domain.task.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.api.homeView.dto.CreateTaskRequestDto;
import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.task.infra.TaskRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateTaskServiceImpl implements CreateTaskService{

    private final TaskRepository taskRepository;

    @Override
    public Task createTaskByCategoryAndBetweenDate(Category category, CreateTaskRequestDto dto) {
        return taskRepository.save(Task.createTask(dto.name(), dto.startDate(),
            dto.endDate(), category));
    }
}
