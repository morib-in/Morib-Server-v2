package org.morib.server.domain.relationship.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.relationship.infra.RelationshipRepository;
import org.morib.server.domain.relationship.infra.type.RelationLevel;
import org.morib.server.domain.user.infra.User;
import org.morib.server.global.exception.AlreadyFriendException;
import org.morib.server.global.exception.AlreadyFriendRequestException;
import org.morib.server.global.message.ErrorMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidateRelationshipServiceImpl implements ValidateRelationshipService {

    private final RelationshipRepository relationshipRepository;

    @Override
    public void validateRelationshipByUserAndFriend(User user, User friend) {
        relationshipRepository.findByUserAndFriend(user.getId(), friend.getId())
                .ifPresent(relationship -> {
                    if (relationship.getRelationLevel() == RelationLevel.CONNECTED) {
                        throw new AlreadyFriendException(ErrorMessage.ALREADY_FRIEND);
                    } else {
                        throw new AlreadyFriendRequestException(ErrorMessage.ALREADY_FRIEND_REQUEST);
                    }
                });

    }
}
