package org.morib.server.domain.relationship.application;

import lombok.RequiredArgsConstructor;
import org.morib.server.domain.relationship.infra.Relationship;
import org.morib.server.domain.relationship.infra.RelationshipRepository;
import org.morib.server.domain.relationship.infra.type.RelationLevel;
import org.morib.server.domain.user.infra.User;
import org.morib.server.global.exception.AlreadyFriendException;
import org.morib.server.global.exception.AlreadyFriendRequestException;
import org.morib.server.global.exception.NotFoundException;
import org.morib.server.global.message.ErrorMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FetchRelationshipServiceImpl implements FetchRelationshipService {

    private final RelationshipRepository relationshipRepository;

    @Override
    public List<Relationship> fetchConnectedRelationship(Long userId) {
        return relationshipRepository.findByUserIdOrFriendIdAndRelationLevel(userId, RelationLevel.CONNECTED);
    }

    @Override
    public List<Relationship> fetchUnconnectedRelationship(Long userId) {
        return relationshipRepository.findByUserIdOrFriendIdAndRelationLevel(userId, RelationLevel.UNCONNECTED);
    }

    @Override
    public Relationship fetchRelationshipByUserIdAndFriendId(Long userId, Long friendId, RelationLevel relationLevel) {
        return relationshipRepository.findByUserIdAndFriendIdAndRelationLevel(userId, friendId, relationLevel).orElseThrow(
                () -> new NotFoundException(ErrorMessage.NOT_FOUND)
        );
    }

    @Override
    public List<Long> fetchConnectedRelationshipAndClassify(Long userId) {
        return relationshipRepository.findByUserIdOrFriendIdAndRelationLevel(userId, RelationLevel.CONNECTED)
                .stream()
                .map(relationship -> relationship.getUser().getId().equals(userId)
                        ? relationship.getFriend().getId()
                        : relationship.getUser().getId())
                .toList();
    }

}
