package org.morib.server.api.timerView.controller;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.morib.server.api.timerView.dto.StopTimerRequestDto;
import org.morib.server.api.timerView.service.TimerViewFacade;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2")
@RequiredArgsConstructor
public class TimerViewController {

    private final TimerViewFacade timerViewFacade;

    @PostMapping("/timer/stop/{taskId}")
    public ResponseEntity stopTimerAndFetchAccumulatedTime(@PathVariable Long taskId, @RequestBody StopTimerRequestDto dto){
        timerViewFacade.stopTimerAndFetch();
        return ResponseEntity.ok("void");
    }


    @GetMapping("/timer/todo-card")
    public ResponseEntity getTodoCards(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate) {
        timerViewFacade.getTodoCard(targetDate);
        return ResponseEntity.ok("void");
    }





}
