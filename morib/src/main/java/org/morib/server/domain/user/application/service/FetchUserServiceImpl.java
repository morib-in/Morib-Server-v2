package org.morib.server.domain.user.application.service;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.user.infra.User;
import org.morib.server.domain.user.infra.UserRepository;
import org.morib.server.global.exception.NotFoundException;
import org.morib.server.global.message.ErrorMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchUserServiceImpl implements FetchUserService {
    
    private final UserRepository userRepository;

    @Override
    public User fetchByUserId(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
    }

    @Override
    public User fetchByUserEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
    }


}
