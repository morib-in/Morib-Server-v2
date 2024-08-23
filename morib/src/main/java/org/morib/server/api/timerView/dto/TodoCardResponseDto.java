package org.morib.server.api.timerView.dto;

import java.util.List;

public record TodoCardResponseDto(Long totalTimeOfToday,List<TaskInTodoCardDto> task) {
}
