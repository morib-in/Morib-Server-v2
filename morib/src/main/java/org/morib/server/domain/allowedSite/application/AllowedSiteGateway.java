package org.morib.server.domain.allowedSite.application;



public interface AllowedSiteGateway {

    void save();

    void findById();

    void findAll();

    void deleteById();

    void deleteAll();

}
