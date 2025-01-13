package org.morib.server.api.modalView.dto;

import java.util.List;

public record FetchUnconnectedRelationshipResponseDto(
        List<FetchRelationshipRequestsResponseDto> send,
        List<FetchRelationshipRequestsResponseDto> receive
) {
    public static FetchUnconnectedRelationshipResponseDto of (List<FetchRelationshipRequestsResponseDto> send, List<FetchRelationshipRequestsResponseDto> receive) {
        return new FetchUnconnectedRelationshipResponseDto(send, receive);
    }
}
