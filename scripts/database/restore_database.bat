@echo off
set ROOT=%~dp0\..\..
if "%~1"=="" (echo Uso: restore_database.bat RUTA_RESPALDO & exit /b 1)
del /q "%ROOT%\data\minimarket_segu-db.*" 2>nul
copy "%~1\minimarket_segu-db.*" "%ROOT%\data\"
echo Base restaurada desde %~1
