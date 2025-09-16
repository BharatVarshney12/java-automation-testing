@echo off
REM Script to run regression tests only
echo Running Regression Tests...

mvn clean test -Dgroups=regression -Dsuite=src/test/resources/testng.xml

echo.
echo Regression test execution completed. Check reports in the reports/ directory.
pause