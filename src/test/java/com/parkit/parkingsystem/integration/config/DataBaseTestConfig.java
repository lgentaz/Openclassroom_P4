package com.parkit.parkingsystem.integration.config;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.config.GetProperties;

public class DataBaseTestConfig extends DataBaseConfig {
    private static final GetProperties dbconfig = new GetProperties();
    private final String url = dbconfig.getProp("testURL");

}
