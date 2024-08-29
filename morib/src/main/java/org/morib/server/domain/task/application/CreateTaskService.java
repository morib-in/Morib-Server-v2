package org.morib.server.domain.task.application;

import java.time.LocalDate;
import org.morib.server.api.homeView.dto.CreateTaskRequestDto;
import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.task.infra.Task;

public interface CreateTaskService {

    void createTaskByCategoryAndBetweenDate(Category category, String name, LocalDate startDate, LocalDate endDate);

}
