package org.morib.server.global.common;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataUtils {
    public static boolean isInRange(LocalDate idxDate, LocalDate startDate, LocalDate endDate) {
        return !idxDate.isBefore(startDate) && !idxDate.isAfter(endDate);
    }
}
