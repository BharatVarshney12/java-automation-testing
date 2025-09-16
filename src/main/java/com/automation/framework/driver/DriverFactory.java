package com.automation.framework.driver;

import com.automation.framework.config.ConfigManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * WebDriver Factory for creating and managing WebDriver instances
 * 
 * @author Automation Framework
 * @version 1.0
 */
public class DriverFactory {
    
    private static final Logger logger = LogManager.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<WebDriverWait> waitThreadLocal = new ThreadLocal<>();
    private static final ConfigManager config = ConfigManager.getInstance();
    
    /**
     * Create WebDriver instance based on browser type
     * @param browser Browser name
     * @return WebDriver instance
     */
    public static WebDriver createDriver(String browser) {
        WebDriver driver = null;
        
        try {
            switch (browser.toLowerCase()) {
                case "chrome":
                    driver = createChromeDriver();
                    break;
                case "firefox":
                    driver = createFirefoxDriver();
                    break;
                case "edge":
                    driver = createEdgeDriver();
                    break;
                case "safari":
                    driver = createSafariDriver();
                    break;
                case "remote":
                    driver = createRemoteDriver();
                    break;
                default:
                    logger.error("Unsupported browser: " + browser);
                    throw new IllegalArgumentException("Browser not supported: " + browser);
            }
            
            configureDriver(driver);
            setDriver(driver);
            logger.info("WebDriver created successfully for browser: " + browser);
            
        } catch (Exception e) {
            logger.error("Failed to create WebDriver for browser: " + browser, e);
            throw new RuntimeException("WebDriver creation failed", e);
        }
        
        return driver;
    }
    
    /**
     * Create Chrome driver with options
     * @return ChromeDriver instance
     */
    private static WebDriver createChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        
        if (config.isHeadless()) {
            options.addArguments("--headless");
        }
        
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-infobars");
        
        return new ChromeDriver(options);
    }
    
    /**
     * Create Firefox driver with options
     * @return FirefoxDriver instance
     */
    private static WebDriver createFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        
        if (config.isHeadless()) {
            options.addArguments("--headless");
        }
        
        options.addArguments("--width=1920");
        options.addArguments("--height=1080");
        
        return new FirefoxDriver(options);
    }
    
    /**
     * Create Edge driver with options
     * @return EdgeDriver instance
     */
    private static WebDriver createEdgeDriver() {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        
        if (config.isHeadless()) {
            options.addArguments("--headless");
        }
        
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        
        return new EdgeDriver(options);
    }
    
    /**
     * Create Safari driver
     * @return SafariDriver instance
     */
    private static WebDriver createSafariDriver() {
        return new SafariDriver();
    }
    
    /**
     * Create Remote WebDriver for grid execution
     * @return RemoteWebDriver instance
     */
    private static WebDriver createRemoteDriver() {
        try {
            String gridUrl = config.getProperty("grid.url", "http://localhost:4444/wd/hub");
            String browser = config.getProperty("remote.browser", "chrome");
            
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            
            return new RemoteWebDriver(new URL(gridUrl), options);
        } catch (MalformedURLException e) {
            logger.error("Invalid grid URL", e);
            throw new RuntimeException("Remote driver creation failed", e);
        }
    }
    
    /**
     * Configure WebDriver with timeouts and settings
     * @param driver WebDriver instance
     */
    private static void configureDriver(WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(
            Duration.ofSeconds(config.getImplicitWait())
        );
        driver.manage().timeouts().pageLoadTimeout(
            Duration.ofSeconds(config.getPageLoadTimeout())
        );
        driver.manage().window().maximize();
        
        // Create WebDriverWait instance
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitWait()));
        setWait(wait);
    }
    
    /**
     * Set WebDriver instance in ThreadLocal
     * @param driver WebDriver instance
     */
    public static void setDriver(WebDriver driver) {
        driverThreadLocal.set(driver);
    }
    
    /**
     * Get WebDriver instance from ThreadLocal
     * @return WebDriver instance
     */
    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }
    
    /**
     * Set WebDriverWait instance in ThreadLocal
     * @param wait WebDriverWait instance
     */
    public static void setWait(WebDriverWait wait) {
        waitThreadLocal.set(wait);
    }
    
    /**
     * Get WebDriverWait instance from ThreadLocal
     * @return WebDriverWait instance
     */
    public static WebDriverWait getWait() {
        return waitThreadLocal.get();
    }
    
    /**
     * Quit WebDriver and clean up ThreadLocal
     */
    public static void quitDriver() {
        WebDriver driver = getDriver();
        if (driver != null) {
            try {
                driver.quit();
                logger.info("WebDriver quit successfully");
            } catch (Exception e) {
                logger.error("Error while quitting WebDriver", e);
            } finally {
                driverThreadLocal.remove();
                waitThreadLocal.remove();
            }
        }
    }
    
    /**
     * Check if WebDriver is initialized
     * @return true if driver is initialized
     */
    public static boolean isDriverInitialized() {
        return getDriver() != null;
    }
}