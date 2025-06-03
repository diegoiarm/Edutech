@echo off
set "SCRIPT_DIR=%~dp0"
echo Iniciando Servicio Académico...
start "Academic Service" cmd /k "cd /d "%SCRIPT_DIR%academic-service" && mvn spring-boot:run"
timeout /t 10
echo Servicio Académico iniciado. 