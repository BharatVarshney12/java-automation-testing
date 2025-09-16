package com.automation.tests;

import com.automation.framework.pages.GoogleMapsPage;
import com.automation.tests.base.BaseTest;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Google Maps Test Suite
 * 
 * @author Automation Framework
 * @version 1.0
 */
@Epic("Google Maps Testing")
@Feature("Navigation and Route Planning")
public class GoogleMapsTests extends BaseTest {
    
    private GoogleMapsPage googleMapsPage;
    
    @Test(description = "Verify Google Maps page loads successfully", 
          groups = {"smoke", "maps"}, 
          priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Story("Page Load Verification")
    @Description("Test to verify that Google Maps page loads successfully and displays correct title")
    public void testGoogleMapsPageLoad() {
        googleMapsPage = new GoogleMapsPage();
        
        String mapsUrl = "https://www.google.com/maps";
        googleMapsPage.navigateToGoogleMaps(mapsUrl);
        
        String pageTitle = googleMapsPage.getGoogleMapsTitle();
        logger.info("Page title: " + pageTitle);
        
        Assert.assertTrue(pageTitle.contains("Google Maps"), 
            "Page title should contain 'Google Maps'");
        
        takeScreenshot("Google Maps Page Loaded");
    }
    
    @Test(description = "Test location search functionality", 
          groups = {"regression", "maps"}, 
          priority = 2,
          dependsOnMethods = "testGoogleMapsPageLoad")
    @Severity(SeverityLevel.NORMAL)
    @Story("Location Search")
    @Description("Test to verify location search functionality works correctly")
    public void testLocationSearch() {
        googleMapsPage = new GoogleMapsPage();
        
        String mapsUrl = "https://www.google.com/maps";
        googleMapsPage.navigateToGoogleMaps(mapsUrl);
        
        String searchLocation = "Times Square, New York, NY";
        googleMapsPage.searchLocation(searchLocation);
        
        // Wait for search results to load
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        String currentUrl = googleMapsPage.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("Times+Square"), 
            "URL should contain the searched location");
        
        takeScreenshot("Location Search Results");
    }
    
    @Test(description = "Test route planning between two locations", 
          groups = {"regression", "maps", "routing"}, 
          priority = 3)
    @Severity(SeverityLevel.NORMAL)
    @Story("Route Planning")
    @Description("Test to verify route planning functionality between two locations")
    public void testRoutePlanning() {
        googleMapsPage = new GoogleMapsPage();
        
        String mapsUrl = "https://www.google.com/maps";
        googleMapsPage.navigateToGoogleMaps(mapsUrl);
        
        // Search for a location first
        googleMapsPage.searchLocation("Central Park, New York");
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Click on Directions
        googleMapsPage.clickDirections();
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Plan route
        String source = "Times Square, New York";
        String destination = "Central Park, New York";
        googleMapsPage.planRoute(source, destination);
        
        // Wait for route calculation
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Verify route is displayed
        boolean isRouteDisplayed = googleMapsPage.isRouteDisplayed();
        Assert.assertTrue(isRouteDisplayed, "Route should be displayed on the map");
        
        // Get route information if available
        String routeInfo = googleMapsPage.getRouteInfo();
        logger.info("Route information: " + routeInfo);
        
        takeScreenshot("Route Planning Results");
    }
    
    @Test(description = "Test navigation to different map views", 
          groups = {"smoke", "maps"}, 
          priority = 4)
    @Severity(SeverityLevel.MINOR)
    @Story("Map Navigation")
    @Description("Test different map view functionalities")
    public void testMapNavigation() {
        googleMapsPage = new GoogleMapsPage();
        
        String mapsUrl = "https://www.google.com/maps";
        googleMapsPage.navigateToGoogleMaps(mapsUrl);
        
        // Test searching for a landmark
        googleMapsPage.searchLocation("Statue of Liberty");
        
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        String pageTitle = googleMapsPage.getGoogleMapsTitle();
        Assert.assertNotNull(pageTitle, "Page title should not be null");
        
        takeScreenshot("Landmark Search");
    }
    
    @Test(description = "Test invalid location search handling", 
          groups = {"negative", "maps"}, 
          priority = 5)
    @Severity(SeverityLevel.MINOR)
    @Story("Error Handling")
    @Description("Test how the application handles invalid location searches")
    public void testInvalidLocationSearch() {
        googleMapsPage = new GoogleMapsPage();
        
        String mapsUrl = "https://www.google.com/maps";
        googleMapsPage.navigateToGoogleMaps(mapsUrl);
        
        // Search for an invalid/non-existent location
        String invalidLocation = "XYZ123InvalidLocation";
        googleMapsPage.searchLocation(invalidLocation);
        
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // The test should complete without errors
        // Google Maps typically handles invalid searches gracefully
        String currentUrl = googleMapsPage.getCurrentUrl();
        Assert.assertNotNull(currentUrl, "Current URL should not be null");
        
        takeScreenshot("Invalid Location Search");
    }
}