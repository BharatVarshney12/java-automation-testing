package com.automation.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

/**
 * Google Maps Page Object Model
 * 
 * @author Automation Framework
 * @version 1.0
 */
public class GoogleMapsPage extends BasePage {
    
    // Page elements locators
    private final By searchBox = By.id("searchboxinput");
    private final By searchButton = By.id("searchbox-searchbutton");
    private final By directionsButton = By.cssSelector("[data-value='Directions']");
    private final By sourceInput = By.xpath("//input[@placeholder='Choose starting point, or click on the map...']");
    private final By destinationInput = By.xpath("//input[@placeholder='Choose destination, or click on the map...']");
    private final By routeInfo = By.cssSelector(".section-directions-trip-duration");
    private final By acceptCookiesButton = By.xpath("//button[contains(text(), 'Accept all')]");
    
    /**
     * Navigate to Google Maps
     * @param url Google Maps URL
     */
    public void navigateToGoogleMaps(String url) {
        navigateToUrl(url);
        handleCookiesPopup();
        logger.info("Navigated to Google Maps successfully");
    }
    
    /**
     * Handle cookies popup if present
     */
    private void handleCookiesPopup() {
        try {
            if (isDisplayed(acceptCookiesButton)) {
                click(acceptCookiesButton);
                logger.info("Accepted cookies popup");
            }
        } catch (Exception e) {
            logger.debug("No cookies popup found or already handled");
        }
    }
    
    /**
     * Search for a location
     * @param location Location to search
     */
    public void searchLocation(String location) {
        enterText(searchBox, location);
        click(searchButton);
        logger.info("Searched for location: " + location);
    }
    
    /**
     * Click on Directions button
     */
    public void clickDirections() {
        click(directionsButton);
        logger.info("Clicked on Directions button");
    }
    
    /**
     * Enter source and destination for route planning
     * @param source Source location
     * @param destination Destination location
     */
    public void planRoute(String source, String destination) {
        // Enter source
        if (isDisplayed(sourceInput)) {
            enterText(sourceInput, source);
        }
        
        // Enter destination
        enterText(destinationInput, destination);
        
        // Press Enter to search for route
        findElement(destinationInput).sendKeys(Keys.ENTER);
        
        logger.info("Planned route from " + source + " to " + destination);
    }
    
    /**
     * Get route information
     * @return Route duration and distance information
     */
    public String getRouteInfo() {
        try {
            waitForElementVisible(routeInfo, 10);
            String info = getText(routeInfo);
            logger.info("Retrieved route information: " + info);
            return info;
        } catch (Exception e) {
            logger.warn("Route information not available");
            return "Route information not available";
        }
    }
    
    /**
     * Verify if route is displayed
     * @return true if route is displayed
     */
    public boolean isRouteDisplayed() {
        try {
            return isDisplayed(routeInfo);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get page title
     * @return Google Maps page title
     */
    public String getGoogleMapsTitle() {
        return getPageTitle();
    }
}