@echo off
REM Script to run all tests
echo Running All Tests...

mvn clean test -Dsuite=src/test/resources/testng.xml

echo.
echo Test execution completed. Check reports in the reports/ directory.
pause