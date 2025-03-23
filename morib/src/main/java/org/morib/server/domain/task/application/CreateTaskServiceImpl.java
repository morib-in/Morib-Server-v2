package org.morib.server.domain.task.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.task.infra.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CreateTaskServiceImpl implements CreateTaskService{

    private final TaskRepository taskRepository;

    @Override
    public void createTaskByCategoryAndBetweenDate(Category category, String name, LocalDate startDate, LocalDate endDate) {
        taskRepository.save(Task.createTask(name, startDate,
            endDate, category));
    }
}
