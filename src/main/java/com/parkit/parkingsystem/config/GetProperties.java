package com.parkit.parkingsystem.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.util.*;

public class GetProperties {
    private static final Logger logger = LogManager.getLogger("GetProperties");

    /**
     * Retrieves and returns the property value stored in config.properties
     *
     * @param key
     *          string of character corresponding to the property type
     * @return value
     *          string of character corresponding to the precific value associated to the key in config.properties file
     */

    public String getProp(String key) {
        Properties prop = new Properties();
        String propFile = "config.properties";

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFile);
        try {
            prop.load(inputStream);
        } catch (IOException ioe) {
            logger.error("Property file '" + propFile + "' not found in the classpath");
        }
        String value = prop.getProperty(key);
        return value;
    }
}
