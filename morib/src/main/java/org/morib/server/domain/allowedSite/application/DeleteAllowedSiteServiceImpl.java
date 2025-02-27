package org.morib.server.domain.allowedSite.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.allowedSite.infra.AllowedSiteRepository;
import org.morib.server.global.exception.NotFoundException;
import org.morib.server.global.message.ErrorMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteAllowedSiteServiceImpl implements
    DeleteAllowedSiteService {

    private final AllowedSiteRepository allowedSiteRepository;

    @Override
    public void delete(Long id) {
        allowedSiteRepository.findById(id).ifPresentOrElse(
            allowedSiteRepository::delete,
            () -> {
                throw new NotFoundException(ErrorMessage.NOT_FOUND);
            }
        );
    }
}
