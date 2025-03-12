package org.morib.server.domain.allowedSite.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.allowedSite.infra.AllowedSite;
import org.morib.server.domain.allowedSite.infra.AllowedSiteRepository;
import org.morib.server.global.exception.DuplicateResourceException;
import org.morib.server.global.exception.NotFoundException;
import org.morib.server.global.message.ErrorMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FetchAllowedSiteServiceImpl implements FetchAllowedSiteService{

    private final AllowedSiteRepository allowedSiteRepository;

    @Override
    public void isExist(String siteUrl, Long allowedGroupId) {
        if (allowedSiteRepository.existsBySiteUrlAndAllowedGroupId(siteUrl, allowedGroupId))
            throw new DuplicateResourceException(ErrorMessage.DUPLICATE_RESOURCE);
    }

    @Override
    public AllowedSite fetchBySiteUrlAndAllowedGroupId(String siteUrl, Long allowedGroupId) {
        return allowedSiteRepository.findBySiteUrlAndAllowedGroupId(siteUrl, allowedGroupId);
    }

    @Override
    public AllowedSite fetchBySiteUrlContainingAndAllowedGroupId(String siteUrl, Long allowedGroupId) {
        return allowedSiteRepository.findBySiteUrlContainingAndAllowedGroupId(siteUrl, allowedGroupId);
    }

    @Override
    public AllowedSite fetchById(Long id) {
        return allowedSiteRepository.findById(id).orElseThrow(
                () -> new NotFoundException(ErrorMessage.NOT_FOUND)
        );
    }

    @Override
    public List<AllowedSite> fetchByDomainContaining(Long allowedGroupId, String topDomainUrl) {
        return allowedSiteRepository.findByAllowedGroupIdAndSiteUrlContaining(allowedGroupId, topDomainUrl);
    }

}
