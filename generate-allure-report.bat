@echo off
REM Script to generate Allure reports
echo Generating Allure Reports...

REM Run tests first
mvn clean test -Dsuite=src/test/resources/testng.xml

REM Generate Allure report
mvn allure:serve

echo.
echo Allure report generation completed.
pause