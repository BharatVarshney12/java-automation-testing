@echo off
REM Script to run smoke tests only
echo Running Smoke Tests...

mvn clean test -Dgroups=smoke -Dsuite=src/test/resources/testng.xml

echo.
echo Smoke test execution completed. Check reports in the reports/ directory.
pause