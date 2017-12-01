package com.mario.sample.reconciliation.config;

import com.mario.sample.reconciliation.exception.ReconciliationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ConfigProperties {

    private static final Logger logger = LoggerFactory.getLogger(ConfigProperties.class);

    private static final String CONFIG_FILE_PROPERTY = "config.file";
    private static final String DEFAULT_CONFIG_FILE_NAME = "config.properties";

    private static final Properties configProperties = init();

    private static Properties init() throws ReconciliationException {
        String configFileLocation = System.getProperty(CONFIG_FILE_PROPERTY);
        InputStream inputStream;
        if (configFileLocation != null) {
            try {
                inputStream = new FileInputStream(configFileLocation);
            } catch (FileNotFoundException e) {
                logger.error(e.getMessage(), e);
                throw new ReconciliationException(e.getMessage());
            }
        } else {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            configFileLocation = loader.getResource(DEFAULT_CONFIG_FILE_NAME).getFile();
            inputStream = loader.getResourceAsStream(DEFAULT_CONFIG_FILE_NAME);
        }
        Properties appProps = new Properties();
        try {
            logger.info("Reading configuration from file: " + configFileLocation);
            appProps.load(inputStream);
        } catch (IOException e) {
            String msg = "An error occurred while trying to read default config properties from " + configFileLocation;
            logger.error(msg, e);
            throw new ReconciliationException(msg, e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                String msg = "An error occurred while trying to close " + configFileLocation;
                logger.error(msg, e);
                throw new ReconciliationException(msg, e);
            }
        }
        return appProps;
    }

    public static String getStringProperty(String key) {
        return configProperties.getProperty(key);
    }

    public static List<String> getListProperty(String key) {
        String value = configProperties.getProperty(key);
        if (value != null) {
            String[] values = value.split("\\s*,\\s*");
            return Arrays.asList(values);
        }
        return new ArrayList<>();
    }

}
