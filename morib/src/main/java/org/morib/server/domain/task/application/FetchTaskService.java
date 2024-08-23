package org.morib.server.domain.task.application;

import java.util.Set;
import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.task.infra.Task;

public interface FetchTaskService {
    void fetch();

    Task fetchById(Long taskId);
}
