package com.parkit.parkingsystem.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.util.*;

public class GetProperties {
    private InputStream inputStream;
    private static final Logger logger = LogManager.getLogger("GetProperties");


    public String getProp(String key) {
        Properties prop = new Properties();
        String propFile = "config.properties";

        inputStream = getClass().getClassLoader().getResourceAsStream(propFile);
        if (inputStream != null) {
            try {
                prop.load(inputStream);
            } catch (IOException ioe) {
                logger.error("Property file '" + propFile + "' not found in the classpath");
            }
        }
        return prop.getProperty(key);

    }
}