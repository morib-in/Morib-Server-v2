package org.morib.server.api.timerView.service.fetch.task;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.task.TaskManager;
import org.morib.server.domain.task.infra.Task;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchTaskServiceImpl implements FetchTaskService {

    private final TaskManager taskManager;

    @Override
    public void fetch() {
        //timer가 멈췄을때 계산해주는 역할을 해야함!

    }

    @Override
    public Task fetchByTaskIdInCategories(Set<Category> categories, Long taskId) {
        //taskManager.findByTaskIdInCategories(categories, taskId);
        return categories.stream()
            .flatMap(category -> category.getTasks().stream())
            .filter(task -> taskManager.isTaskHaveSameTaskId(task, taskId))
            .findFirst().orElseThrow(() -> new IllegalArgumentException("해당 task가 없습니다."));
    }


}
