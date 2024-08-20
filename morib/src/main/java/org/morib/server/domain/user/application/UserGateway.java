package org.morib.server.domain.user.application;



public interface UserGateway {

    void save();

    void findById();

    void findAll();

    void deleteById();

    void deleteAll();

}
