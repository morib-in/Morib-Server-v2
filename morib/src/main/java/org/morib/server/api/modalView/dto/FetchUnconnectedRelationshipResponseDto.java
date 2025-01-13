package org.morib.server.api.modalView.dto;

import java.util.List;

public record FetchUnconnectedRelationshipResponseDto(
        List<FetchRelationshipRequestResponseDto> send,
        List<FetchRelationshipRequestResponseDto> receive
) {
    public static FetchUnconnectedRelationshipResponseDto of (List<FetchRelationshipRequestResponseDto> send, List<FetchRelationshipRequestResponseDto> receive) {
        return new FetchUnconnectedRelationshipResponseDto(send, receive);
    }
}
