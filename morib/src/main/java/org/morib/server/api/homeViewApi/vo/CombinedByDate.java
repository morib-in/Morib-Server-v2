package org.morib.server.api.homeViewApi.vo;

import lombok.Value;

import java.time.LocalDate;
import java.util.List;

@Value
public class CombinedByDate {
    LocalDate date;
    List<TasksByCategory> combined;

    public static CombinedByDate of(LocalDate date, List<TasksByCategory> combined) {
        return new CombinedByDate(date, combined);
    }
}
