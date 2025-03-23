package org.morib.server.domain.task.application;

import org.morib.server.domain.category.infra.Category;

import java.time.LocalDate;

public interface CreateTaskService {

    void createTaskByCategoryAndBetweenDate(Category category, String name, LocalDate startDate, LocalDate endDate);

}
