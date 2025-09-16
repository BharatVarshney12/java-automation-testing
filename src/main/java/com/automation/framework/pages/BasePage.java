package com.automation.framework.pages;

import com.automation.framework.driver.DriverFactory;
import com.automation.framework.utils.ExtentReportManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Base Page class containing common functionality for all page objects
 * 
 * @author Automation Framework
 * @version 1.0
 */
public abstract class BasePage {
    
    protected final Logger logger = LogManager.getLogger(this.getClass());
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Actions actions;
    
    /**
     * Constructor to initialize BasePage
     */
    public BasePage() {
        this.driver = DriverFactory.getDriver();
        this.wait = DriverFactory.getWait();
        this.actions = new Actions(driver);
    }
    
    /**
     * Find element with explicit wait
     * @param locator Element locator
     * @return WebElement
     */
    protected WebElement findElement(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            logger.debug("Element found: " + locator.toString());
            return element;
        } catch (Exception e) {
            logger.error("Element not found: " + locator.toString(), e);
            ExtentReportManager.logFail("Element not found: " + locator.toString());
            throw e;
        }
    }
    
    /**
     * Find elements with explicit wait
     * @param locator Element locator
     * @return List of WebElements
     */
    protected List<WebElement> findElements(By locator) {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            List<WebElement> elements = driver.findElements(locator);
            logger.debug("Elements found: " + elements.size() + " for locator: " + locator.toString());
            return elements;
        } catch (Exception e) {
            logger.error("Elements not found: " + locator.toString(), e);
            throw e;
        }
    }
    
    /**
     * Click on element with explicit wait
     * @param locator Element locator
     */
    protected void click(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            element.click();
            logger.info("Clicked on element: " + locator.toString());
            ExtentReportManager.logInfo("Clicked on element: " + locator.toString());
        } catch (Exception e) {
            logger.error("Failed to click on element: " + locator.toString(), e);
            ExtentReportManager.logFail("Failed to click on element: " + locator.toString());
            throw e;
        }
    }
    
    /**
     * Enter text in input field
     * @param locator Element locator
     * @param text Text to enter
     */
    protected void enterText(By locator, String text) {
        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            element.clear();
            element.sendKeys(text);
            logger.info("Entered text '" + text + "' in element: " + locator.toString());
            ExtentReportManager.logInfo("Entered text '" + text + "' in element: " + locator.toString());
        } catch (Exception e) {
            logger.error("Failed to enter text in element: " + locator.toString(), e);
            ExtentReportManager.logFail("Failed to enter text in element: " + locator.toString());
            throw e;
        }
    }
    
    /**
     * Get text from element
     * @param locator Element locator
     * @return Element text
     */
    protected String getText(By locator) {
        try {
            WebElement element = findElement(locator);
            String text = element.getText();
            logger.info("Retrieved text '" + text + "' from element: " + locator.toString());
            return text;
        } catch (Exception e) {
            logger.error("Failed to get text from element: " + locator.toString(), e);
            throw e;
        }
    }
    
    /**
     * Get attribute value from element
     * @param locator Element locator
     * @param attributeName Attribute name
     * @return Attribute value
     */
    protected String getAttribute(By locator, String attributeName) {
        try {
            WebElement element = findElement(locator);
            String attributeValue = element.getAttribute(attributeName);
            logger.info("Retrieved attribute '" + attributeName + "' value '" + attributeValue + "' from element: " + locator.toString());
            return attributeValue;
        } catch (Exception e) {
            logger.error("Failed to get attribute from element: " + locator.toString(), e);
            throw e;
        }
    }
    
    /**
     * Check if element is displayed
     * @param locator Element locator
     * @return true if element is displayed
     */
    protected boolean isDisplayed(By locator) {
        try {
            WebElement element = findElement(locator);
            boolean isDisplayed = element.isDisplayed();
            logger.info("Element display status: " + isDisplayed + " for locator: " + locator.toString());
            return isDisplayed;
        } catch (Exception e) {
            logger.debug("Element not displayed: " + locator.toString());
            return false;
        }
    }
    
    /**
     * Check if element is enabled
     * @param locator Element locator
     * @return true if element is enabled
     */
    protected boolean isEnabled(By locator) {
        try {
            WebElement element = findElement(locator);
            boolean isEnabled = element.isEnabled();
            logger.info("Element enabled status: " + isEnabled + " for locator: " + locator.toString());
            return isEnabled;
        } catch (Exception e) {
            logger.error("Failed to check if element is enabled: " + locator.toString(), e);
            return false;
        }
    }
    
    /**
     * Wait for element to be visible
     * @param locator Element locator
     * @param timeoutSeconds Timeout in seconds
     */
    protected void waitForElementVisible(By locator, int timeoutSeconds) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            customWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            logger.info("Element became visible: " + locator.toString());
        } catch (Exception e) {
            logger.error("Element did not become visible within " + timeoutSeconds + " seconds: " + locator.toString(), e);
            throw e;
        }
    }
    
    /**
     * Wait for element to be clickable
     * @param locator Element locator
     * @param timeoutSeconds Timeout in seconds
     */
    protected void waitForElementClickable(By locator, int timeoutSeconds) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            customWait.until(ExpectedConditions.elementToBeClickable(locator));
            logger.info("Element became clickable: " + locator.toString());
        } catch (Exception e) {
            logger.error("Element did not become clickable within " + timeoutSeconds + " seconds: " + locator.toString(), e);
            throw e;
        }
    }
    
    /**
     * Select option from dropdown by visible text
     * @param locator Dropdown locator
     * @param optionText Option text to select
     */
    protected void selectByText(By locator, String optionText) {
        try {
            WebElement dropdown = findElement(locator);
            Select select = new Select(dropdown);
            select.selectByVisibleText(optionText);
            logger.info("Selected option '" + optionText + "' from dropdown: " + locator.toString());
            ExtentReportManager.logInfo("Selected option '" + optionText + "' from dropdown");
        } catch (Exception e) {
            logger.error("Failed to select option from dropdown: " + locator.toString(), e);
            ExtentReportManager.logFail("Failed to select option from dropdown");
            throw e;
        }
    }
    
    /**
     * Select option from dropdown by value
     * @param locator Dropdown locator
     * @param value Option value to select
     */
    protected void selectByValue(By locator, String value) {
        try {
            WebElement dropdown = findElement(locator);
            Select select = new Select(dropdown);
            select.selectByValue(value);
            logger.info("Selected option with value '" + value + "' from dropdown: " + locator.toString());
            ExtentReportManager.logInfo("Selected option with value '" + value + "' from dropdown");
        } catch (Exception e) {
            logger.error("Failed to select option by value from dropdown: " + locator.toString(), e);
            ExtentReportManager.logFail("Failed to select option by value from dropdown");
            throw e;
        }
    }
    
    /**
     * Hover over element
     * @param locator Element locator
     */
    protected void hoverOver(By locator) {
        try {
            WebElement element = findElement(locator);
            actions.moveToElement(element).perform();
            logger.info("Hovered over element: " + locator.toString());
            ExtentReportManager.logInfo("Hovered over element");
        } catch (Exception e) {
            logger.error("Failed to hover over element: " + locator.toString(), e);
            throw e;
        }
    }
    
    /**
     * Double click on element
     * @param locator Element locator
     */
    protected void doubleClick(By locator) {
        try {
            WebElement element = findElement(locator);
            actions.doubleClick(element).perform();
            logger.info("Double clicked on element: " + locator.toString());
            ExtentReportManager.logInfo("Double clicked on element");
        } catch (Exception e) {
            logger.error("Failed to double click on element: " + locator.toString(), e);
            throw e;
        }
    }
    
    /**
     * Right click on element
     * @param locator Element locator
     */
    protected void rightClick(By locator) {
        try {
            WebElement element = findElement(locator);
            actions.contextClick(element).perform();
            logger.info("Right clicked on element: " + locator.toString());
            ExtentReportManager.logInfo("Right clicked on element");
        } catch (Exception e) {
            logger.error("Failed to right click on element: " + locator.toString(), e);
            throw e;
        }
    }
    
    /**
     * Scroll to element
     * @param locator Element locator
     */
    protected void scrollToElement(By locator) {
        try {
            WebElement element = findElement(locator);
            actions.scrollToElement(element).perform();
            logger.info("Scrolled to element: " + locator.toString());
        } catch (Exception e) {
            logger.error("Failed to scroll to element: " + locator.toString(), e);
            throw e;
        }
    }
    
    /**
     * Get page title
     * @return Page title
     */
    protected String getPageTitle() {
        String title = driver.getTitle();
        logger.info("Page title: " + title);
        return title;
    }
    
    /**
     * Get current URL
     * @return Current URL
     */
    public String getCurrentUrl() {
        String url = driver.getCurrentUrl();
        logger.info("Current URL: " + url);
        return url;
    }
    
    /**
     * Navigate to URL
     * @param url URL to navigate to
     */
    protected void navigateToUrl(String url) {
        try {
            driver.get(url);
            logger.info("Navigated to URL: " + url);
            ExtentReportManager.logInfo("Navigated to URL: " + url);
        } catch (Exception e) {
            logger.error("Failed to navigate to URL: " + url, e);
            ExtentReportManager.logFail("Failed to navigate to URL: " + url);
            throw e;
        }
    }
    
    /**
     * Refresh the page
     */
    protected void refreshPage() {
        try {
            driver.navigate().refresh();
            logger.info("Page refreshed");
            ExtentReportManager.logInfo("Page refreshed");
        } catch (Exception e) {
            logger.error("Failed to refresh page", e);
            throw e;
        }
    }
}