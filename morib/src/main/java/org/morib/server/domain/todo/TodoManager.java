package org.morib.server.domain.todo;

import org.morib.server.annotation.Manager;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.todo.infra.Todo;

import java.util.List;

@Manager
public class TodoManager {

    public void updateTask(Todo todo, List<Task> tasks) {
        todo.updateTasks(tasks);
    }
}
