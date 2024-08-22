package org.morib.server.api.modalViewApi.service.create;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.category.application.CategoryGateway;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CreateCategoryServiceImpl implements CreateCategoryService {
    private final CategoryGateway categoryGateway;
    // private final MsetGateway msetGateway;

    @Override
    public void execute() {
        // 요청으로 들어온 카테고리 정보와 모립세트 정보를 받아 Gateway로 DB에 저장
    }
}
