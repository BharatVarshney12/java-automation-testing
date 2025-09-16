package com.automation.framework.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ExtentReports utility for generating HTML reports
 * 
 * @author Automation Framework
 * @version 1.0
 */
public class ExtentReportManager {
    
    private static final Logger logger = LogManager.getLogger(ExtentReportManager.class);
    private static ExtentReports extentReports;
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private static final String REPORTS_DIR = "reports/";
    
    /**
     * Initialize ExtentReports
     */
    public static void initReports() {
        if (extentReports == null) {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String reportPath = REPORTS_DIR + "ExtentReport_" + timestamp + ".html";
            
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setDocumentTitle("Automation Test Report");
            sparkReporter.config().setReportName("Test Execution Report");
            sparkReporter.config().setTimeStampFormat("dd/MM/yyyy hh:mm:ss");
            
            extentReports = new ExtentReports();
            extentReports.attachReporter(sparkReporter);
            
            // Add system information
            extentReports.setSystemInfo("OS", System.getProperty("os.name"));
            extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
            extentReports.setSystemInfo("User", System.getProperty("user.name"));
            
            logger.info("ExtentReports initialized: " + reportPath);
        }
    }
    
    /**
     * Create a new test in the report
     * @param testName Test name
     * @param description Test description
     */
    public static void createTest(String testName, String description) {
        ExtentTest test = extentReports.createTest(testName, description);
        extentTest.set(test);
        logger.info("Created test in report: " + testName);
    }
    
    /**
     * Create a new test with category
     * @param testName Test name
     * @param description Test description
     * @param category Test category
     */
    public static void createTest(String testName, String description, String category) {
        ExtentTest test = extentReports.createTest(testName, description);
        test.assignCategory(category);
        extentTest.set(test);
        logger.info("Created test with category '" + category + "': " + testName);
    }
    
    /**
     * Get current test instance
     * @return ExtentTest instance
     */
    public static ExtentTest getTest() {
        return extentTest.get();
    }
    
    /**
     * Log info message
     * @param message Info message
     */
    public static void logInfo(String message) {
        if (getTest() != null) {
            getTest().log(Status.INFO, message);
        }
    }
    
    /**
     * Log pass message
     * @param message Pass message
     */
    public static void logPass(String message) {
        if (getTest() != null) {
            getTest().log(Status.PASS, message);
        }
    }
    
    /**
     * Log fail message
     * @param message Fail message
     */
    public static void logFail(String message) {
        if (getTest() != null) {
            getTest().log(Status.FAIL, message);
        }
    }
    
    /**
     * Log skip message
     * @param message Skip message
     */
    public static void logSkip(String message) {
        if (getTest() != null) {
            getTest().log(Status.SKIP, message);
        }
    }
    
    /**
     * Log warning message
     * @param message Warning message
     */
    public static void logWarning(String message) {
        if (getTest() != null) {
            getTest().log(Status.WARNING, message);
        }
    }
    
    /**
     * Add screenshot to report
     * @param screenshotPath Screenshot file path
     * @param description Screenshot description
     */
    public static void addScreenshot(String screenshotPath, String description) {
        if (getTest() != null && screenshotPath != null) {
            try {
                getTest().addScreenCaptureFromPath(screenshotPath, description);
                logger.info("Screenshot added to report: " + screenshotPath);
            } catch (Exception e) {
                logger.error("Failed to add screenshot to report: " + e.getMessage());
            }
        }
    }
    
    /**
     * Mark test as passed
     * @param message Pass message
     */
    public static void markTestPassed(String message) {
        logPass(message);
        logger.info("Test marked as PASSED: " + message);
    }
    
    /**
     * Mark test as failed
     * @param message Fail message
     * @param screenshotPath Optional screenshot path
     */
    public static void markTestFailed(String message, String screenshotPath) {
        logFail(message);
        if (screenshotPath != null) {
            addScreenshot(screenshotPath, "Failure Screenshot");
        }
        logger.error("Test marked as FAILED: " + message);
    }
    
    /**
     * Mark test as skipped
     * @param message Skip message
     */
    public static void markTestSkipped(String message) {
        logSkip(message);
        logger.info("Test marked as SKIPPED: " + message);
    }
    
    /**
     * Flush reports and save to disk
     */
    public static void flushReports() {
        if (extentReports != null) {
            extentReports.flush();
            logger.info("ExtentReports flushed and saved");
        }
    }
    
    /**
     * Clean up test from ThreadLocal
     */
    public static void removeTest() {
        extentTest.remove();
    }
}