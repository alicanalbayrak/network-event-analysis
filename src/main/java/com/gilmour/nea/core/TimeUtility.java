package com.gilmour.nea.core;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Created by gilmour on Jul, 2017.
 */
public class TimeUtility {

    private static long timestampInHourResolution(long timestamp){
        Instant instant = Instant.ofEpochMilli(timestamp);
        instant = instant.truncatedTo(ChronoUnit.HOURS);
        return instant.toEpochMilli();
    }
}
