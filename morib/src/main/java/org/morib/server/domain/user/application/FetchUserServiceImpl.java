package org.morib.server.domain.user.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.user.infra.User;
import org.morib.server.domain.user.infra.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchUserServiceImpl implements FetchUserService {
    
    private final UserRepository userRepository;

    @Override
    public User fetchByUserId(Long  userId) {
        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
    }
}
