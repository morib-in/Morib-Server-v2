package org.morib.server.domain.todo.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.mset.infra.MsetRepository;
import org.morib.server.domain.todo.infra.TodoRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TodoDataServiceImpl implements TodoDataService {

    private final TodoRepository todoRepository;

    @Override
    public void save() {

    }

    @Override
    public void findById() {

    }

    @Override
    public void findAll() {

    }

    @Override
    public void deleteById() {

    }

    @Override
    public void deleteAll() {

    }
}
