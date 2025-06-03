@echo off
set "SCRIPT_DIR=%~dp0"
echo Iniciando Servicio de Pagos...
start "Payment Service" cmd /k "cd /d "%SCRIPT_DIR%payment-service" && mvn spring-boot:run"
timeout /t 10
echo Servicio de Pagos iniciado. 