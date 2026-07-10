@echo off
set ROOT=%~dp0\..\..
set DEST=%~1
if "%DEST%"=="" set DEST=%ROOT%\backups\manual
if not exist "%DEST%" mkdir "%DEST%"
copy "%ROOT%\data\minimarket_segu-db.*" "%DEST%\"
echo Respaldo creado en %DEST%
