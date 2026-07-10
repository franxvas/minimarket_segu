@echo off
if "%JAVA_HOME%"=="" (echo Debe definir JAVA_HOME & exit /b 1)
if "%CATALINA_HOME%"=="" (echo Debe definir CATALINA_HOME & exit /b 1)
where mvn >nul 2>nul || (echo Maven no esta disponible & exit /b 1)
pushd "%~dp0\..\.."
call mvn clean package -DskipTests -Djacoco.skip=true || exit /b 1
copy /Y "target\minimarket_segu.war" "%CATALINA_HOME%\webapps\minimarket_segu.war"
popd
