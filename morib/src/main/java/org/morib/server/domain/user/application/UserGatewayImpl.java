package org.morib.server.domain.user.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.user.infra.User;
import org.morib.server.domain.user.infra.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserGatewayImpl implements UserGateway {

    private final UserRepository userRepository;

    @Override
    public void save() {

    }

    @Override
    public User findById(Long userId) {
        // 이후 예외처리 추가
        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
    }

    @Override
    public void findAll() {

    }

    @Override
    public void deleteById() {

    }

    @Override
    public void deleteAll() {

    }
}
