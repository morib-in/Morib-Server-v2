package org.morib.server.domain.category;

import org.morib.server.annotation.Manager;
import org.morib.server.domain.category.infra.Category;
import org.morib.server.domain.task.infra.Task;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Manager
public class CategoryManager {

    public Map<LocalDate, List<Category>> classifyByDate(List<Category> categories, LocalDate startDate, LocalDate endDate) {
        Map<LocalDate, List<Category>> result = new LinkedHashMap<>();
        for (LocalDate idxDate = startDate; !idxDate.isAfter(endDate); idxDate = idxDate.plusDays(1)) {
            LocalDate copiedIdxDate = idxDate;
            result.put(copiedIdxDate, categories.stream()
                    .filter(category -> isInRange(copiedIdxDate, category.getStartDate(), category.getEndDate()))
                    .collect(Collectors.toList()));
        }
        return result;
    }

    public Map<LocalDate, Map<Category, List<Task>>> classifyTaskByCategory(Map<LocalDate, List<Category>> categories) {
        Map<LocalDate, Map<Category, List<Task>>> result = new LinkedHashMap<>();
        for (Map.Entry<LocalDate, List<Category>> entry : categories.entrySet()) {
            LocalDate idxDate = entry.getKey();
            Map<Category, List<Task>> categoryTaskMap = new LinkedHashMap<>();
            for (Category category : entry.getValue()) {
                List<Task> taskList = new ArrayList<>();
                for (Task task : category.getTasks()) {
                    if (isInRange(idxDate, task.getStartDate(), task.getEndDate())) {
                        taskList.add(task);
                    }
                }
                categoryTaskMap.put(category, taskList);
            }
            result.put(idxDate, categoryTaskMap);
        }
        return result;
    }

    private boolean isInRange(LocalDate idxDate, LocalDate startDate, LocalDate endDate) {
        return (idxDate.isEqual(startDate) || idxDate.isAfter(startDate)) &&
                (idxDate.isEqual(endDate) || idxDate.isBefore(endDate));
    }
}
