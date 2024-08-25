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
        return taskRepository.save(Task.builder()
            .name(dto.name())
            .startDate(dto.startDate())
            .endDate(dto.endDate())
            .category(category)
            .build());
    }
}
