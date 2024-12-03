package org.morib.server.domain.user.application.service;


import org.morib.server.domain.user.infra.User;

public interface FetchUserService {
    User fetchByUserId(Long id);
}
