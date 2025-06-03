@echo off
set "SCRIPT_DIR=%~dp0"
echo Iniciando Servicio de Identidad...
start "Identity Service" cmd /k "cd /d "%SCRIPT_DIR%identity-service" && mvn spring-boot:run"
timeout /t 10
echo Servicio de Identidad iniciado. 