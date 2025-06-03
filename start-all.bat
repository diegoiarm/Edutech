@echo off
set "SCRIPT_DIR=%~dp0"
echo Iniciando todos los servicios...

echo 1. Iniciando Servidor Eureka...
call "%SCRIPT_DIR%start-eureka.bat"

echo 2. Iniciando Servicio de Identidad...
call "%SCRIPT_DIR%start-identity.bat"

echo 3. Iniciando Servicio Académico...
call "%SCRIPT_DIR%start-academic.bat"

echo 4. Iniciando Servicio de Calificaciones...
call "%SCRIPT_DIR%start-marks.bat"

echo 5. Iniciando Servicio de Pagos...
call "%SCRIPT_DIR%start-payment.bat"

echo 6. Iniciando Servicio de Soporte...
call "%SCRIPT_DIR%start-support.bat"

echo Todos los servicios han sido iniciados.
echo Puede acceder al Dashboard de Eureka en: http://localhost:8761
echo.
echo Presione cualquier tecla para cerrar esta ventana...
pause > nul 