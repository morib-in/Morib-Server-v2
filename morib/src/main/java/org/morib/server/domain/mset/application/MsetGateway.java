package org.morib.server.domain.mset.application;



public interface MsetGateway {

    void save();

    void findById();

    void findAll();

    void deleteById();

    void deleteAll();

}
