@echo off
set URL=%~1
if "%URL%"=="" set URL=http://localhost:8080/minimarket_segu/
curl -L -f "%URL%" -o "%TEMP%\minimarket-smoke.html" || exit /b 1
findstr /I "minimarket" "%TEMP%\minimarket-smoke.html" >nul || exit /b 1
echo OK: %URL%
