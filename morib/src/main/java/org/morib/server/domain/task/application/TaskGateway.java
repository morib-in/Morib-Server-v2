package org.morib.server.domain.task.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.task.infra.Task;
import org.morib.server.domain.task.infra.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;


public interface TaskGateway {
    void save();
    void findById();
    void findAll();
    void deleteById();
    void deleteAll();
}
