@echo off
if "%CATALINA_HOME%"=="" (echo Debe definir CATALINA_HOME & exit /b 1)
call "%CATALINA_HOME%\bin\shutdown.bat"
