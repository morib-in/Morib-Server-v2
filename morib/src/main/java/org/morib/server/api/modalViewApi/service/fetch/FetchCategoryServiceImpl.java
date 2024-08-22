package org.morib.server.api.modalViewApi.service.fetch;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.category.application.CategoryGateway;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FetchCategoryServiceImpl implements FetchCategoryService {
    private final CategoryGateway categoryGateway;

    @Override
    public void execute() {
        // gateway로 해당 카테고리를 조회 후 리턴
    }
}
