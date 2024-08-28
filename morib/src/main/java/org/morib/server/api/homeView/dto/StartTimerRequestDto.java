package org.morib.server.api.homeView.dto;

import java.util.List;

public record StartTimerRequestDto(
        List<Long> taskIdList
){

}
