package org.morib.server.api.modalView.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateRelationshipRequestDto(
        @NotBlank
        @Email
        String friendEmail
) {
}
