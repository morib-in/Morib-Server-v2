package org.morib.server.domain.allowedSite.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.allowedSite.infra.AllowedSiteRepository;
import org.morib.server.global.exception.DuplicateResourceException;
import org.morib.server.global.message.ErrorMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchAllowedSiteServiceImpl implements FetchAllowedSiteService{

    private final AllowedSiteRepository allowedSiteRepository;

    public void isExist(String siteUrl) {
        if (allowedSiteRepository.existsBySiteUrl(siteUrl))
            throw new DuplicateResourceException(ErrorMessage.DUPLICATE_RESOURCE);
    }

}
