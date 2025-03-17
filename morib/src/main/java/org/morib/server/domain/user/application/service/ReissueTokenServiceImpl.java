package org.morib.server.domain.user.application.service;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.user.application.dto.ReissueTokenServiceDto;
import org.morib.server.domain.user.infra.User;
import org.morib.server.domain.user.infra.UserRepository;
import org.morib.server.global.exception.NotFoundException;
import org.morib.server.global.jwt.JwtService;
import org.morib.server.global.message.ErrorMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReissueTokenServiceImpl implements ReissueTokenService{
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public ReissueTokenServiceDto reissue(String refreshToken) {

        User findUser = userRepository.findByRefreshToken(refreshToken).orElseThrow(
                () -> new NotFoundException(ErrorMessage.NOT_FOUND)
        );
        String newRefreshToken = jwtService.createRefreshToken();
        jwtService.updateRefreshToken(findUser.getId(), newRefreshToken);
        return ReissueTokenServiceDto.of(jwtService.createAccessToken(findUser.getId()), newRefreshToken);
    }


}
