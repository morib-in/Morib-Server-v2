package org.morib.server.domain.user.application.service;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.user.infra.WaitingUserWindow;
import org.morib.server.domain.user.infra.WaitingUserWindowRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateWaitingUserWindowServiceImpl implements CreateWaitingUserWindowService{

    private final WaitingUserWindowRepository waitingUserWindowRepository;

    @Override
    public void createWaitingUserWindow(String email) {
        waitingUserWindowRepository.save(WaitingUserWindow.create(email));
    }
}
