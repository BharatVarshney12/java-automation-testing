package com.automation.framework.utils;

import com.automation.framework.config.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG Test Listener to handle test events
 * 
 * @author Automation Framework
 * @version 1.0
 */
public class TestListener implements ITestListener {
    
    private static final Logger logger = LogManager.getLogger(TestListener.class);
    
    @Override
    public void onTestStart(ITestResult result) {
        logger.info("Starting test: " + result.getMethod().getMethodName());
        logger.info("Test Description: " + result.getMethod().getDescription());
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test PASSED: " + result.getMethod().getMethodName());
        
        // Take screenshot on success if configured
        if (shouldTakeScreenshotOnPass()) {
            try {
                ScreenshotUtils.takeScreenshot(result.getMethod().getMethodName() + "_PASSED");
            } catch (Exception e) {
                logger.error("Failed to take screenshot on test success: " + e.getMessage());
            }
        }
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("Test FAILED: " + result.getMethod().getMethodName());
        logger.error("Failure reason: " + result.getThrowable().getMessage());
        
        // Take screenshot on failure
        try {
            String screenshotName = result.getMethod().getMethodName() + "_FAILED";
            String screenshotPath = ScreenshotUtils.takeScreenshot(screenshotName);
            logger.info("Screenshot captured: " + screenshotPath);
            
            // Attach screenshot to test result for reporting
            System.setProperty("screenshot.path", screenshotPath);
        } catch (Exception e) {
            logger.error("Failed to take screenshot on test failure: " + e.getMessage());
        }
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("Test SKIPPED: " + result.getMethod().getMethodName());
        if (result.getThrowable() != null) {
            logger.warn("Skip reason: " + result.getThrowable().getMessage());
        }
    }
    
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        logger.warn("Test FAILED but within success percentage: " + result.getMethod().getMethodName());
    }
    
    /**
     * Check if screenshots should be taken on test pass
     * @return true if screenshots should be taken on pass
     */
    private boolean shouldTakeScreenshotOnPass() {
        try {
            ConfigManager configManager = ConfigManager.getInstance();
            return Boolean.parseBoolean(configManager.getProperty("report.screenshots.on.pass", "false"));
        } catch (Exception e) {
            logger.error("Error checking screenshot configuration: " + e.getMessage());
            return false;
        }
    }
}