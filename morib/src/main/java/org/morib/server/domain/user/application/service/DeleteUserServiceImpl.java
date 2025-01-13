package org.morib.server.domain.user.application.service;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.user.infra.User;
import org.morib.server.domain.user.infra.UserRepository;
import org.morib.server.global.exception.NotFoundException;
import org.morib.server.global.message.ErrorMessage;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DeleteUserServiceImpl implements DeleteUserService {

    private final UserRepository userRepository;

    @Override
    public void delete(Long userId) {
        User findUser = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(ErrorMessage.NOT_FOUND)
        );
        userRepository.delete(findUser);
    }
}
