@echo off
set "SCRIPT_DIR=%~dp0"
echo Iniciando Servicio de Calificaciones...
start "Marks Service" cmd /k "cd /d "%SCRIPT_DIR%marks-service" && mvn spring-boot:run"
timeout /t 10
echo Servicio de Calificaciones iniciado. 