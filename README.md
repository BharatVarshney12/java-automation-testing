# Java Automation Testing Framework

[![Build Status](https://github.com/BharatVarshney12/java-automation-testing/workflows/CI/badge.svg)](https://github.com/BharatVarshney12/java-automation-testing/actions)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-11%2B-orange.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.6%2B-blue.svg)](https://maven.apache.org/)
[![Selenium](https://img.shields.io/badge/Selenium-4.15.0-green.svg)](https://selenium.dev/)

A comprehensive Java-based automation testing framework using Maven, Selenium WebDriver, TestNG, and advanced reporting capabilities.

## 🚀 Features

- **Page Object Model (POM)** design pattern
- **Cross-browser testing** support (Chrome, Firefox, Edge)
- **Parallel test execution** with TestNG
- **Data-driven testing** with Excel/CSV support
- **Comprehensive reporting** with ExtentReports and Allure
- **Screenshot capture** on test failures
- **Configuration management** with properties files
- **Logging** with Log4j2
- **CI/CD ready** with Maven

## 🛠️ Technologies Used

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 11+ | Programming Language |
| Maven | 3.6+ | Build Tool |
| Selenium WebDriver | 4.15.0 | Web Automation |
| TestNG | 7.8.0 | Test Framework |
| ExtentReports | 5.1.1 | HTML Reporting |
| Allure | 2.24.0 | Advanced Reporting |
| WebDriverManager | 5.6.2 | Driver Management |
| Log4j2 | 2.20.0 | Logging |
| Apache POI | 5.2.4 | Excel Data Handling |

## 📁 Project Structure

```
bharattesting123/
├── src/
│   ├── main/java/com/automation/framework/
│   │   ├── config/
│   │   │   └── ConfigManager.java           # Configuration management
│   │   ├── driver/
│   │   │   └── DriverFactory.java           # WebDriver factory
│   │   ├── pages/
│   │   │   ├── BasePage.java                # Base page class
│   │   │   └── GoogleMapsPage.java          # Google Maps page object
│   │   └── utils/
│   │       ├── ExtentReportManager.java     # ExtentReports manager
│   │       ├── ScreenshotUtils.java         # Screenshot utilities
│   │       ├── TestListener.java            # TestNG listener
│   │       └── ExcelDataProvider.java       # Excel data provider
│   └── test/
│       ├── java/com/automation/tests/
│       │   ├── base/
│       │   │   └── BaseTest.java            # Base test class
│       │   └── GoogleMapsTests.java         # Google Maps test cases
│       └── resources/
│           ├── config.properties            # Configuration properties
│           ├── log4j2.xml                   # Logging configuration
│           ├── testng.xml                   # TestNG suite configuration
│           └── testdata.csv                 # Test data
├── reports/                                 # Generated reports
├── screenshots/                             # Test screenshots
├── logs/                                    # Application logs
├── pom.xml                                  # Maven configuration
└── *.bat                                    # Execution scripts
```

## 🚦 Quick Start

### Prerequisites

1. **Java 11+** installed
2. **Maven 3.6+** installed
3. **Chrome/Firefox** browser installed
4. **Git** (optional, for version control)

### Installation

1. **Clone or download** the project
2. **Navigate** to the project directory
3. **Install dependencies**:
   ```bash
   mvn clean install
   ```

### Running Tests

#### Option 1: Using Batch Scripts

- **All Tests**: Double-click `run-all-tests.bat`
- **Smoke Tests**: Double-click `run-smoke-tests.bat`
- **Regression Tests**: Double-click `run-regression-tests.bat`
- **Generate Allure Report**: Double-click `generate-allure-report.bat`

#### Option 2: Using Maven Commands

```bash
# Run all tests
mvn clean test

# Run specific test groups
mvn clean test -Dgroups=smoke
mvn clean test -Dgroups=regression

# Run with specific browser
mvn clean test -Dbrowser=firefox

# Run specific test class
mvn clean test -Dtest=GoogleMapsTests

# Run specific test method
mvn clean test -Dtest=GoogleMapsTests#testGoogleMapsPageLoad
```

#### Option 3: Using TestNG XML

```bash
mvn clean test -Dsuite=src/test/resources/testng.xml
```

## 📊 Reports

### ExtentReports
- **Location**: `reports/extent-report.html`
- **Features**: Test results, screenshots, execution time
- **Auto-opens**: After test execution

### Allure Reports
- **Generate**: Run `generate-allure-report.bat`
- **Features**: Advanced analytics, trends, history
- **Command**: `mvn allure:serve`

### TestNG Reports
- **Location**: `test-output/index.html`
- **Features**: Basic test results and suite information

## ⚙️ Configuration

### Browser Configuration (`config.properties`)

```properties
# Browser settings
browser.default=chrome
browser.headless=false
browser.window.maximize=true
browser.implicit.wait=10
```

### Test Data Configuration

```properties
# Test data settings
test.data.file=testdata.csv
test.data.sheet=TestData
```

### Reporting Configuration

```properties
# Report settings
report.extent.title=Automation Test Report
report.screenshots.on.failure=true
```

## 🧪 Test Cases

### Google Maps Test Suite

| Test Case | Description | Priority |
|-----------|-------------|----------|
| `testGoogleMapsPageLoad` | Verify Google Maps page loads | Critical |
| `testLocationSearch` | Test location search functionality | Normal |
| `testRoutePlanning` | Test route planning between locations | Normal |
| `testMapNavigation` | Test map navigation features | Minor |
| `testInvalidLocationSearch` | Test error handling | Minor |

## 🔧 Framework Features

### Page Object Model
- Separation of test logic and page elements
- Reusable page components
- Easy maintenance

### Driver Management
- Automatic driver download and setup
- Support for multiple browsers
- ThreadLocal driver instances for parallel execution

### Configuration Management
- Centralized configuration
- Environment-specific settings
- Runtime parameter override

### Reporting
- Multiple report formats
- Screenshot attachment
- Test execution metrics

### Data-Driven Testing
- Excel/CSV data support
- Parameterized test execution
- Test data management

## 🚀 Advanced Usage

### Parallel Execution

Modify `testng.xml` for parallel execution:

```xml
<suite name="ParallelSuite" parallel="methods" thread-count="3">
```

### Custom Properties

Override default properties:

```bash
mvn test -Dbrowser=firefox -Dheadless=true
```

### Environment Configuration

Create environment-specific property files:
- `config-dev.properties`
- `config-test.properties`
- `config-prod.properties`

## 📝 Adding New Tests

### 1. Create Page Object

```java
public class NewPage extends BasePage {
    // Page elements and methods
}
```

### 2. Create Test Class

```java
public class NewTests extends BaseTest {
    @Test
    public void testNewFeature() {
        // Test implementation
    }
}
```

### 3. Update TestNG XML

Add new test class to `testng.xml`:

```xml
<class name="com.automation.tests.NewTests"/>
```

## 🐛 Troubleshooting

### Common Issues

1. **Driver Issues**: Ensure WebDriverManager is properly configured
2. **Browser Compatibility**: Update browser versions
3. **Timeout Issues**: Adjust wait times in configuration
4. **Report Generation**: Check file permissions for report directories

### Debug Mode

Enable debug logging in `log4j2.xml`:

```xml
<Root level="DEBUG">
```

## 📞 Support

For issues or questions:
1. Check the logs in the `logs/` directory
2. Review the generated reports
3. Ensure all prerequisites are met
4. Verify configuration settings

## 🤝 Contributing

1. Follow the existing code structure
2. Add appropriate JavaDoc comments
3. Include unit tests for new features
4. Update documentation

## 📄 License

This project is for educational and testing purposes.

---

**Happy Testing! 🎉**