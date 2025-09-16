package com.automation.tests.base;

import com.automation.framework.config.ConfigManager;
import com.automation.framework.driver.DriverFactory;
import com.automation.framework.utils.ExtentReportManager;
import com.automation.framework.utils.ScreenshotUtils;
import io.qameta.allure.Attachment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;

/**
 * Base Test class containing common setup and teardown methods
 * 
 * @author Automation Framework
 * @version 1.0
 */
public class BaseTest {
    
    protected final Logger logger = LogManager.getLogger(this.getClass());
    protected ConfigManager config;
    
    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        logger.info("Starting test suite execution");
        ExtentReportManager.initReports();
        config = ConfigManager.getInstance();
    }
    
    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        logger.info("Starting test class: " + this.getClass().getSimpleName());
    }
    
    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method) {
        logger.info("Starting test method: " + method.getName());
        
        // Get browser from system property or config
        String browser = System.getProperty("browser", config.getBrowser());
        
        // Create WebDriver instance
        DriverFactory.createDriver(browser);
        
        // Create test in ExtentReports
        String testName = method.getName();
        String testDescription = getTestDescription(method);
        String category = getTestCategory(method);
        
        if (category != null) {
            ExtentReportManager.createTest(testName, testDescription, category);
        } else {
            ExtentReportManager.createTest(testName, testDescription);
        }
        
        ExtentReportManager.logInfo("Test started with browser: " + browser);
    }
    
    @AfterMethod(alwaysRun = true)
    public void afterMethod(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        
        try {
            if (result.getStatus() == ITestResult.FAILURE) {
                logger.error("Test failed: " + testName);
                
                // Take screenshot on failure
                if (config.isScreenshotOnFailure()) {
                    String screenshotPath = ScreenshotUtils.takeFailureScreenshot(testName);
                    if (screenshotPath != null) {
                        ExtentReportManager.markTestFailed(
                            "Test failed: " + result.getThrowable().getMessage(), 
                            screenshotPath
                        );
                        attachScreenshotToAllure(screenshotPath);
                    } else {
                        ExtentReportManager.markTestFailed(
                            "Test failed: " + result.getThrowable().getMessage(), 
                            null
                        );
                    }
                }
            } else if (result.getStatus() == ITestResult.SUCCESS) {
                logger.info("Test passed: " + testName);
                ExtentReportManager.markTestPassed("Test completed successfully");
            } else if (result.getStatus() == ITestResult.SKIP) {
                logger.info("Test skipped: " + testName);
                ExtentReportManager.markTestSkipped("Test was skipped");
            }
        } finally {
            // Clean up WebDriver
            DriverFactory.quitDriver();
            ExtentReportManager.removeTest();
        }
    }
    
    @AfterClass(alwaysRun = true)
    public void afterClass() {
        logger.info("Completed test class: " + this.getClass().getSimpleName());
    }
    
    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        logger.info("Completed test suite execution");
        ExtentReportManager.flushReports();
    }
    
    /**
     * Get test description from Test annotation
     * @param method Test method
     * @return Test description
     */
    private String getTestDescription(Method method) {
        Test testAnnotation = method.getAnnotation(Test.class);
        if (testAnnotation != null && !testAnnotation.description().isEmpty()) {
            return testAnnotation.description();
        }
        return "Test method: " + method.getName();
    }
    
    /**
     * Get test category/group from Test annotation
     * @param method Test method
     * @return Test category
     */
    private String getTestCategory(Method method) {
        Test testAnnotation = method.getAnnotation(Test.class);
        if (testAnnotation != null && testAnnotation.groups().length > 0) {
            return String.join(", ", testAnnotation.groups());
        }
        return null;
    }
    
    /**
     * Attach screenshot to Allure report
     * @param screenshotPath Screenshot file path
     */
    @Attachment(value = "Failure Screenshot", type = "image/png")
    private byte[] attachScreenshotToAllure(String screenshotPath) {
        try {
            return ScreenshotUtils.class.getResourceAsStream(screenshotPath).readAllBytes();
        } catch (Exception e) {
            logger.error("Failed to attach screenshot to Allure: " + e.getMessage());
            return new byte[0];
        }
    }
    
    /**
     * Navigate to base URL from configuration
     */
    protected void navigateToBaseUrl() {
        String baseUrl = config.getBaseUrl();
        DriverFactory.getDriver().get(baseUrl);
        logger.info("Navigated to base URL: " + baseUrl);
        ExtentReportManager.logInfo("Navigated to base URL: " + baseUrl);
    }
    
    /**
     * Take screenshot during test execution
     * @param description Screenshot description
     */
    protected void takeScreenshot(String description) {
        String screenshotPath = ScreenshotUtils.takeScreenshot(description);
        if (screenshotPath != null) {
            ExtentReportManager.addScreenshot(screenshotPath, description);
        }
    }
}