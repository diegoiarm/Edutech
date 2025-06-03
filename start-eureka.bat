@echo off
set "SCRIPT_DIR=%~dp0"
echo Iniciando Servidor Eureka...
start "Eureka Server" cmd /k "cd /d "%SCRIPT_DIR%eureka-server" && mvn spring-boot:run"
timeout /t 10
echo Servidor Eureka iniciado. 