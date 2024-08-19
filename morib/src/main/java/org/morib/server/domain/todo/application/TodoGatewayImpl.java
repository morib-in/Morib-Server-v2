package org.morib.server.domain.todo.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.todo.infra.TodoRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TodoGatewayImpl implements TodoGateway {

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
