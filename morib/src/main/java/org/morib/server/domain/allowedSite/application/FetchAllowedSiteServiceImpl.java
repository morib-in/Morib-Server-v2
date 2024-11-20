package org.morib.server.domain.allowedSite.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.morib.server.domain.allowedSite.infra.AllowedSite;
import org.morib.server.domain.allowedSite.infra.AllowedSiteRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchAllowedSiteServiceImpl implements FetchAllowedSiteService{

    private final AllowedSiteRepository allowedSiteRepository;


}
