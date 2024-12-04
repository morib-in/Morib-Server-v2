package org.morib.server.api.modalView.dto;

import java.util.List;

public record FetchUnconnectedRelationshipResponseDto(
        List<FetchRelationshipResponseDto> send,
        List<FetchRelationshipResponseDto> receive
) {
    public static FetchUnconnectedRelationshipResponseDto of (List<FetchRelationshipResponseDto> send, List<FetchRelationshipResponseDto> receive) {
        return new FetchUnconnectedRelationshipResponseDto(send, receive);
    }
}
