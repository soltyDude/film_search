package com.example.kino_search.property;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertyManager {

    private static final Logger logger = Logger.getLogger(PropertyManager.class.getName());
    private static Properties properties = new Properties();

    static {

        try (InputStream input = PropertyManager.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                logger.severe("Property file 'db.properties' not found in the classpath");
                throw new RuntimeException("Property file 'db.properties' not found in the classpath");
            }
            properties.load(input);
            logger.info("Properties file loaded successfully");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading properties file", e);
            throw new RuntimeException("Error loading properties file", e);
        }
    }

    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            logger.warning("Property '" + key + "' is not defined in the properties file");
        } else {
            logger.info("Property '" + key + "' retrieved successfully");
        }
        return value;
    }
}
