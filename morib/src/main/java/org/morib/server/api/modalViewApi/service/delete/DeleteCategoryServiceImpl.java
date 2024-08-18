package org.morib.server.api.modalViewApi.service.delete;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.category.infra.CategoryGateway;
import org.morib.server.domain.task.infra.TaskGateway;
import org.morib.server.domain.timer.infra.TimerGateway;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DeleteCategoryServiceImpl implements DeleteCategoryService{
    private final CategoryGateway categoryGateway;
    private final TaskGateway taskGateway;
//    private final MsetGateway msetGateway;
    private final TimerGateway timerGateway;

    @Override
    public void execute() {
        // 각 Gateway로 해당 카테고리와 연관된 모든 테이블 삭제
    }
}
