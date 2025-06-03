@echo off
set "SCRIPT_DIR=%~dp0"
echo Iniciando Servicio de Soporte...
start "Support Service" cmd /k "cd /d "%SCRIPT_DIR%support-service" && mvn spring-boot:run"
timeout /t 10
echo Servicio de Soporte iniciado. 