package org.morib.server.api.homeViewApi.service.create.task;

import lombok.RequiredArgsConstructor;
import org.morib.server.api.homeViewApi.service.create.task.CreateTaskService;
import org.morib.server.domain.task.application.TaskGateway;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CreateTaskServiceImpl implements CreateTaskService {
    private final TaskGateway taskGateway;

    @Override
    public void create() {
        // 요청으로 들어온 카테고리 정보와 태스크 정보를 받아 Gateway로 DB에 저장
    }
}
