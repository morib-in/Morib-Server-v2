package org.morib.server.api.loginView.facade;

import lombok.RequiredArgsConstructor;
import org.morib.server.annotation.Facade;
import org.morib.server.domain.relationship.application.DeleteRelationshipService;
import org.morib.server.domain.relationship.application.FetchRelationshipService;
import org.morib.server.domain.relationship.infra.Relationship;
import org.morib.server.domain.relationship.infra.RelationshipRepository;
import org.morib.server.domain.user.UserManager;
import org.morib.server.domain.user.application.dto.ReissueTokenServiceDto;
import org.morib.server.domain.user.application.service.CreateWaitingUserWindowService;
import org.morib.server.domain.user.application.service.DeleteUserService;
import org.morib.server.domain.user.application.service.FetchUserService;
import org.morib.server.domain.user.application.service.ReissueTokenService;
import org.morib.server.domain.user.infra.User;
import org.morib.server.global.oauth2.service.CustomOAuth2UserService;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Facade
public class UserAuthFacade {

    private final ReissueTokenService reissueTokenService;
    private final FetchUserService fetchUserService;
    private final DeleteUserService deleteUserService;
    private final UserManager userManager;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final FetchRelationshipService fetchRelationshipService;
    private final RelationshipRepository relationshipRepository;
    private final CreateWaitingUserWindowService createWaitingUserWindowService;

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
        customOAuth2UserService.withdrawInGoogle(findUser.getSocial_refreshToken());
        List<Relationship> toDelete = new ArrayList<>();
        toDelete.addAll(fetchRelationshipService.fetchUnconnectedRelationship(userId));
        toDelete.addAll(fetchRelationshipService.fetchConnectedRelationship(userId));
        relationshipRepository.deleteAll(toDelete);
        deleteUserService.delete(userId);
    }

    @Transactional
    public void createWaitingUserWindow(String email) {
        createWaitingUserWindowService.createWaitingUserWindow(email);
    }

}
