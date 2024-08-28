package org.morib.server.global.common;

import java.time.LocalDate;

public class DataUtils {
    public static boolean isInRange(LocalDate idxDate, LocalDate startDate, LocalDate endDate) {
        return !idxDate.isBefore(startDate) && !idxDate.isAfter(endDate);
    }
}

