package org.morib.server.domain.allowedSite.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.morib.server.domain.allowedSite.infra.AllowedSite;
import org.morib.server.domain.allowedSite.infra.AllowedSiteRepository;
import org.morib.server.domain.allowedSite.infra.type.OwnerType;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchAllowedSiteServiceImpl implements FetchAllowedSiteService{

    private final AllowedSiteRepository allowedSiteRepository;


    @Override
    public List<AllowedSite> fetchByCategoryId(Long categoryId) {
        return allowedSiteRepository.findByOwnerIdAndOwnerType(categoryId, OwnerType.CATEGORY);
    }

    @Override
    public List<AllowedSite> fetchByTaskId(Long taskId) {
        return allowedSiteRepository.findByOwnerIdAndOwnerType(taskId, OwnerType.TASK);
    }
}
