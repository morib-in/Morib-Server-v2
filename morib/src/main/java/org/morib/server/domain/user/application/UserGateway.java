package org.morib.server.domain.user.application;


import org.morib.server.domain.user.infra.User;

public interface UserGateway {

    void save();

    User findById(Long userId);

    void findAll();

    void deleteById();

    void deleteAll();

}
