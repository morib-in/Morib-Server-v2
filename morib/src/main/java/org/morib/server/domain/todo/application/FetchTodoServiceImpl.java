package org.morib.server.domain.todo.application;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchTodoServiceImpl implements FetchTodoService {


    @Override
    public void fetch(LocalDate targetDate) {

    }
}
