package org.morib.server.api.homeViewApi.service.fetch.home;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.category.CategoryReader;
import org.morib.server.domain.task.TaskReader;
import org.morib.server.domain.timer.TimerReader;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FetchHomeServiceImpl implements FetchHomeService {

    private final CategoryReader categoryReader;
    private final TaskReader taskReader;
    private final TimerReader timerReader;

    @Override
    public void execute() {
        categoryReader.fetchCategories();
        taskReader.fetch();
        timerReader.fetch();
    }


}
