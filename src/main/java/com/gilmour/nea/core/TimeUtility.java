package com.gilmour.nea.core;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Created by gilmour on Jul, 2017.
 */
public class TimeUtility {

    public static final long HOUR_IN_MILLISECONDS = 60 * 60 * 1000;

    public static long timestampInHourResolution(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        instant = instant.truncatedTo(ChronoUnit.HOURS);
        return instant.toEpochMilli();
    }

    public static long timeStampInGivenResoulution(long timestamp, int resolutionInHour) {
        return timestamp - (timestamp % (resolutionInHour * HOUR_IN_MILLISECONDS));
    }
}
