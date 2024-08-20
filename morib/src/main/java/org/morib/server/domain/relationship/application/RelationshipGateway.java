package org.morib.server.domain.relationship.application;



public interface RelationshipGateway {

    void save();

    void findById();

    void findAll();

    void deleteById();

    void deleteAll();

}
