package org.morib.server.domain.task.application;

import org.morib.server.api.homeView.dto.CreateTaskRequestDto;
import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.task.infra.Task;

public interface CreateTaskService {

    Task createTaskByCategoryAndBetweenDate(Category category, CreateTaskRequestDto dto);

}
