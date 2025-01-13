package org.morib.server.api.loginView.facade;

import lombok.RequiredArgsConstructor;
import org.morib.server.annotation.Facade;
import org.morib.server.domain.user.UserManager;
import org.morib.server.domain.user.application.dto.ReissueTokenServiceDto;
import org.morib.server.domain.user.application.service.DeleteUserService;
import org.morib.server.domain.user.application.service.FetchUserService;
import org.morib.server.domain.user.application.service.ReissueTokenService;
import org.morib.server.domain.user.infra.User;
import org.morib.server.global.exception.UnauthorizedException;
import org.morib.server.global.message.ErrorMessage;
import org.morib.server.global.oauth2.service.CustomOAuth2UserService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Facade
public class UserAuthFacade {

    private final ReissueTokenService reissueTokenService;
    private final FetchUserService fetchUserService;
    private final DeleteUserService deleteUserService;
    private final UserManager userManager;
    private final CustomOAuth2UserService customOAuth2UserService;

    public ReissueTokenServiceDto reissue(String refreshToken) {
        return reissueTokenService.reissue(refreshToken);
    }

    // TODO : 이후 google rtk 무효화 로직 추가
    @Transactional
    public void logout(Long userId) {
        User findUser = fetchUserService.fetchByUserId(userId);
        userManager.invalidateRefreshToken(findUser);
    }

    @Transactional
    public void withdraw(Long userId) {
        User findUser = fetchUserService.fetchByUserId(userId);
        customOAuth2UserService.withdrawInGoogle(findUser.getRefreshToken());
        deleteUserService.delete(userId);
    }


}
