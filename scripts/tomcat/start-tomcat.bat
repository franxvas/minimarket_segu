@echo off
if "%JAVA_HOME%"=="" (echo Debe definir JAVA_HOME & exit /b 1)
if "%CATALINA_HOME%"=="" (echo Debe definir CATALINA_HOME & exit /b 1)
call "%CATALINA_HOME%\bin\startup.bat"
