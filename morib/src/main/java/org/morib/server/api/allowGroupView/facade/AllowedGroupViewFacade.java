package org.morib.server.api.allowGroupView.facade;

import lombok.RequiredArgsConstructor;
import org.morib.server.annotation.Facade;
import org.morib.server.domain.allowedGroup.application.DeleteAllowedGroupService;
import org.springframework.transaction.annotation.Transactional;

@Facade
@RequiredArgsConstructor
public class AllowedGroupViewFacade {

    private final DeleteAllowedGroupService deleteAllowedGroupService;

    @Transactional // 이후 분리 진행했을때는 해당 point에서 분산 트랜잭션 관련해서 고려해볼 수 있는 부분
    public void deleteAllowedServiceSet(Long groupId) {
        deleteAllowedGroupService.deleteAllowedGroupById(groupId);
    }
}
