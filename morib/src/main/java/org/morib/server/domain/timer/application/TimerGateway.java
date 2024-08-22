package org.morib.server.domain.timer.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.timer.infra.Timer;
import org.morib.server.domain.timer.infra.TimerRepository;
import org.springframework.stereotype.Service;


public interface TimerGateway {
    void save();
    void findById();
    void findAll();
    void deleteById();
    void deleteAll();
}
