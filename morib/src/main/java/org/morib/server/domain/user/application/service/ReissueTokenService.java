package org.morib.server.domain.user.application.service;


import org.morib.server.domain.user.application.dto.ReissueTokenServiceDto;

public interface ReissueTokenService {
    ReissueTokenServiceDto reissue(String refreshToken);
}
