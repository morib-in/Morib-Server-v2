package org.morib.server.api.timerView.service.fetch.user;


import org.morib.server.domain.user.infra.User;

public interface FetchUserService {
    User fetchByUserId(Long id);


}
