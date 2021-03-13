package com.parkit.parkingsystem.constants;

import com.parkit.parkingsystem.config.GetProperties;

public class Fare {
    private static final GetProperties fareConfig = new GetProperties();
    public static final double BIKE_RATE_PER_HOUR = Double.parseDouble(fareConfig.getProp("bikeRate"));
    public static final double CAR_RATE_PER_HOUR = Double.parseDouble(fareConfig.getProp("carRate"));
}
