package org.morib.server.domain.todo;

import java.util.Set;
import org.morib.server.annotation.Manager;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.todo.infra.Todo;

@Manager
public class TodoManager {

    public void updateTask(Todo todo, Set<Task> tasks) {
        todo.updateTasks(tasks);
    }
}
