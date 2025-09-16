package com.automation.framework.utils;

import com.automation.framework.config.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for handling test data from Excel files
 * 
 * @author Automation Framework
 * @version 1.0
 */
public class ExcelDataProvider {
    
    private static final Logger logger = LogManager.getLogger(ExcelDataProvider.class);
    private static final ConfigManager configManager = ConfigManager.getInstance();
    
    /**
     * Read test data from Excel file
     * 
     * @param filePath Path to Excel file
     * @param sheetName Name of the sheet
     * @return List of test data maps
     */
    public static List<Map<String, String>> readTestData(String filePath, String sheetName) {
        List<Map<String, String>> testData = new ArrayList<>();
        
        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fileInputStream)) {
            
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                logger.error("Sheet not found: " + sheetName);
                return testData;
            }
            
            // Get header row
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                logger.error("Header row not found in sheet: " + sheetName);
                return testData;
            }
            
            // Get all headers
            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(getCellValue(cell));
            }
            
            // Read data rows
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row dataRow = sheet.getRow(i);
                if (dataRow == null) continue;
                
                Map<String, String> rowData = new HashMap<>();
                for (int j = 0; j < headers.size(); j++) {
                    Cell cell = dataRow.getCell(j);
                    String cellValue = (cell != null) ? getCellValue(cell) : "";
                    rowData.put(headers.get(j), cellValue);
                }
                testData.add(rowData);
            }
            
            logger.info("Successfully read " + testData.size() + " rows from " + filePath);
            
        } catch (IOException e) {
            logger.error("Error reading Excel file: " + filePath, e);
        }
        
        return testData;
    }
    
    /**
     * Read test data from default Excel file and sheet
     * 
     * @return List of test data maps
     */
    public static List<Map<String, String>> readDefaultTestData() {
        String filePath = "src/test/resources/" + configManager.getProperty("test.data.file", "testdata.xlsx");
        String sheetName = configManager.getProperty("test.data.sheet", "TestData");
        return readTestData(filePath, sheetName);
    }
    
    /**
     * Get specific test data by test case name
     * 
     * @param testCaseName Name of the test case
     * @return Test data map for the specific test case
     */
    public static Map<String, String> getTestDataByName(String testCaseName) {
        List<Map<String, String>> allTestData = readDefaultTestData();
        
        for (Map<String, String> testData : allTestData) {
            if (testCaseName.equals(testData.get("TestCase"))) {
                return testData;
            }
        }
        
        logger.warn("Test data not found for test case: " + testCaseName);
        return new HashMap<>();
    }
    
    /**
     * Get cell value as string
     * 
     * @param cell Excel cell
     * @return Cell value as string
     */
    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((long) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
    
    /**
     * Check if Excel file exists
     * 
     * @param filePath Path to Excel file
     * @return true if file exists, false otherwise
     */
    public static boolean isFileExists(String filePath) {
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            fileInputStream.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}