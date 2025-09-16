@echo off
echo ===============================================
echo     Java Automation Framework Demo
echo ===============================================
echo.
echo This is a comprehensive Java automation testing framework
echo featuring Maven, Selenium WebDriver, TestNG, and advanced reporting.
echo.
echo Available Commands:
echo.
echo 1. run-all-tests.bat        - Run complete test suite
echo 2. run-smoke-tests.bat      - Run smoke tests only  
echo 3. run-regression-tests.bat - Run regression tests only
echo 4. generate-allure-report.bat - Generate Allure reports
echo.
echo Quick Test Commands:
echo.
echo Single Test:
echo mvn test -Dtest=GoogleMapsTests#testGoogleMapsPageLoad
echo.
echo By Groups:
echo mvn test -Dgroups=smoke
echo mvn test -Dgroups=regression
echo.
echo Different Browsers:
echo mvn test -Dbrowser=firefox
echo mvn test -Dbrowser=chrome
echo.
echo Reports will be generated in:
echo - reports/ExtentReport_[timestamp].html  (ExtentReports)
echo - test-output/index.html                 (TestNG Reports)
echo - target/allure-results/                 (Allure Raw Data)
echo.
echo Screenshots are saved in: screenshots/
echo Logs are saved in: logs/automation.log
echo.
echo Framework Features:
echo ✓ Page Object Model Design Pattern
echo ✓ Cross-browser Testing (Chrome, Firefox, Edge)
echo ✓ Parallel Test Execution
echo ✓ Data-driven Testing
echo ✓ Comprehensive Reporting (ExtentReports + Allure)
echo ✓ Screenshot Capture on Failures
echo ✓ Configuration Management
echo ✓ Advanced Logging with Log4j2
echo ✓ CI/CD Ready
echo.
echo ===============================================
echo     Framework Successfully Set Up!
echo ===============================================
echo.
pause