package org.morib.server.domain.permission.application;



public interface PermissionGateway {

    void save();

    void findById();

    void findAll();

    void deleteById();

    void deleteAll();

}
