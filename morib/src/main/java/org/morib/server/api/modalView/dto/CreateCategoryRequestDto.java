package org.morib.server.api.modalView.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCategoryRequestDto(
        @NotBlank  String name) {
}
