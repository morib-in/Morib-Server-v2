package org.morib.server.domain.user.application;


import org.morib.server.domain.user.infra.User;

public interface FetchUserService {
    User fetchByUserId(Long id);


}
