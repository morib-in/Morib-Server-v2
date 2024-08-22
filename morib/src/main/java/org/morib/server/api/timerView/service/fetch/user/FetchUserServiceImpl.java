package org.morib.server.api.timerView.service.fetch.user;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.morib.server.domain.todo.application.TodoGateway;
import org.morib.server.domain.user.application.UserGateway;
import org.morib.server.domain.user.infra.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FetchUserServiceImpl implements FetchUserService {

    private final UserGateway userGateway;

    @Override
    public User fetchByUserId(Long  userId) {
        return userGateway.findById(userId);
    }
}
