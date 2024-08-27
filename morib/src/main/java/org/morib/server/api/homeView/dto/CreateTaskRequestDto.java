package org.morib.server.api.homeView.dto;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public record CreateTaskRequestDto(
        String name,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

}
