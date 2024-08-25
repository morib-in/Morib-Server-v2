package org.morib.server.domain.todo;

import java.util.Set;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.todo.infra.Todo;
import org.springframework.stereotype.Service;

@Service
public class TodoManager {

    public void updateTask(Todo todo, Set<Task> tasks) {
        todo.updateTasks(tasks);
    }
}
