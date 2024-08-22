package org.morib.server.api.homeViewApi.service.fetch.task;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.task.application.TaskGateway;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FetchTaskServiceImpl implements FetchTaskService {
    private final TaskGateway taskGateway;

    @Override
    public void fetch() {

    }
}
