package org.morib.server.api.timerView.dto;

import jakarta.validation.constraints.NotNull;
import org.morib.server.annotation.IsToday;

import java.time.LocalDate;

public record TimerRequestDto(
        @NotNull Long taskId,
        @NotNull @IsToday LocalDate targetDate
) {
} 