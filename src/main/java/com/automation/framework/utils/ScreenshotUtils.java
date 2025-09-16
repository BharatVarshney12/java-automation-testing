package com.automation.framework.utils;

import com.automation.framework.driver.DriverFactory;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Screenshot utility for capturing and managing screenshots
 * 
 * @author Automation Framework
 * @version 1.0
 */
public class ScreenshotUtils {
    
    private static final Logger logger = LogManager.getLogger(ScreenshotUtils.class);
    private static final String SCREENSHOT_DIR = "reports/screenshots/";
    
    /**
     * Take screenshot and save to reports directory
     * @param testName Test name for screenshot filename
     * @return Screenshot file path
     */
    public static String takeScreenshot(String testName) {
        WebDriver driver = DriverFactory.getDriver();
        if (driver == null) {
            logger.warn("WebDriver is not initialized, cannot take screenshot");
            return null;
        }
        
        try {
            // Create screenshots directory if it doesn't exist
            File screenshotDir = new File(SCREENSHOT_DIR);
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }
            
            // Generate filename with timestamp
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = testName.replaceAll("[^a-zA-Z0-9]", "_") + "_" + timestamp + ".png";
            String filePath = SCREENSHOT_DIR + fileName;
            
            // Take screenshot
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
            File destFile = new File(filePath);
            
            FileUtils.copyFile(sourceFile, destFile);
            logger.info("Screenshot saved: " + filePath);
            
            return filePath;
            
        } catch (IOException e) {
            logger.error("Failed to take screenshot: " + e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Take screenshot for failed test
     * @param testName Test name
     * @return Screenshot file path
     */
    public static String takeFailureScreenshot(String testName) {
        String fileName = "FAILED_" + testName;
        return takeScreenshot(fileName);
    }
    
    /**
     * Take screenshot with custom filename
     * @param customName Custom filename (without extension)
     * @return Screenshot file path
     */
    public static String takeScreenshotWithName(String customName) {
        return takeScreenshot(customName);
    }
}