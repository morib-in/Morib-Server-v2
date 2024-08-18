package org.morib.server.api.timerView.dto;

import java.util.List;

public record TodoCardResponseDto(int totalTimeOfToday,List<TaskInTodoCardDto> task) {
}
