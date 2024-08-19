package org.morib.server.domain.todo.application;



public interface TodoGateway {

    void save();

    void findById();

    void findAll();

    void deleteById();

    void deleteAll();

}
