package org.morib.server.api.timerView.service.fetch.task;

import java.util.Set;
import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.task.infra.Task;

public interface FetchTaskService {
    void fetch();

    Task fetchByTaskIdInCategories(Set<Category> categories, Long taskId);
}
