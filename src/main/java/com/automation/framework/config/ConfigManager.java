package com.automation.framework.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Configuration Manager for handling application properties and settings
 * 
 * @author Automation Framework
 * @version 1.0
 */
public class ConfigManager {
    
    private static final Logger logger = LogManager.getLogger(ConfigManager.class);
    private static Properties properties;
    private static ConfigManager instance;
    
    // Configuration file paths
    private static final String CONFIG_FILE_PATH = "src/test/resources/config.properties";
    private static final String TEST_DATA_PATH = "src/test/resources/testdata/";
    
    private ConfigManager() {
        loadProperties();
    }
    
    /**
     * Get singleton instance of ConfigManager
     * @return ConfigManager instance
     */
    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }
    
    /**
     * Load properties from configuration file
     */
    private void loadProperties() {
        properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH);
            properties.load(fis);
            fis.close();
            logger.info("Configuration properties loaded successfully");
        } catch (IOException e) {
            logger.error("Failed to load configuration properties: " + e.getMessage());
            // Load default properties if config file not found
            loadDefaultProperties();
        }
    }
    
    /**
     * Load default properties if config file is not available
     */
    private void loadDefaultProperties() {
        properties.setProperty("browser", "chrome");
        properties.setProperty("headless", "false");
        properties.setProperty("implicit.wait", "10");
        properties.setProperty("explicit.wait", "20");
        properties.setProperty("page.load.timeout", "30");
        properties.setProperty("base.url", "https://www.google.com");
        properties.setProperty("screenshot.on.failure", "true");
        logger.info("Default properties loaded");
    }
    
    /**
     * Get property value by key
     * @param key Property key
     * @return Property value
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    /**
     * Get property value with default
     * @param key Property key
     * @param defaultValue Default value if key not found
     * @return Property value or default value
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Get integer property value
     * @param key Property key
     * @return Integer value
     */
    public int getIntProperty(String key) {
        return Integer.parseInt(getProperty(key));
    }
    
    /**
     * Get boolean property value
     * @param key Property key
     * @return Boolean value
     */
    public boolean getBooleanProperty(String key) {
        return Boolean.parseBoolean(getProperty(key));
    }
    
    /**
     * Get browser name from properties
     * @return Browser name
     */
    public String getBrowser() {
        return getProperty("browser", "chrome").toLowerCase();
    }
    
    /**
     * Check if headless mode is enabled
     * @return true if headless mode is enabled
     */
    public boolean isHeadless() {
        return getBooleanProperty("headless");
    }
    
    /**
     * Get base URL for application
     * @return Base URL
     */
    public String getBaseUrl() {
        return getProperty("base.url");
    }
    
    /**
     * Get implicit wait timeout
     * @return Implicit wait timeout in seconds
     */
    public int getImplicitWait() {
        return getIntProperty("browser.implicit.wait");
    }
    
    /**
     * Get explicit wait timeout
     * @return Explicit wait timeout in seconds
     */
    public int getExplicitWait() {
        return getIntProperty("browser.explicit.wait");
    }
    
    /**
     * Get page load timeout
     * @return Page load timeout in seconds
     */
    public int getPageLoadTimeout() {
        return getIntProperty("browser.page.load.timeout");
    }
    
    /**
     * Check if screenshot should be taken on failure
     * @return true if screenshot on failure is enabled
     */
    public boolean isScreenshotOnFailure() {
        return getBooleanProperty("screenshot.on.failure");
    }
    
    /**
     * Load test data from JSON file
     * @param fileName JSON file name
     * @param clazz Target class type
     * @param <T> Generic type
     * @return Parsed object
     */
    public <T> T loadTestData(String fileName, Class<T> clazz) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File file = new File(TEST_DATA_PATH + fileName);
            return mapper.readValue(file, clazz);
        } catch (IOException e) {
            logger.error("Failed to load test data from " + fileName + ": " + e.getMessage());
            throw new RuntimeException("Test data loading failed", e);
        }
    }
    
    /**
     * Get environment specific property
     * @param key Property key
     * @return Environment specific property value
     */
    public String getEnvironmentProperty(String key) {
        String env = System.getProperty("env", "dev");
        String envKey = env + "." + key;
        return getProperty(envKey, getProperty(key));
    }
}