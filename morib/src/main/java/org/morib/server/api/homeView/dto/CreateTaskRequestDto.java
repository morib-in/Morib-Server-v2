package org.morib.server.api.homeView.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record CreateTaskRequestDto(
        String name,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

}
