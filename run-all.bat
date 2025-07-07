@echo off
echo ===== Iniciando Eureka Server =====
start "eureka" mvn -f eureka spring-boot:run

timeout /t 5 /nobreak > nul

echo ===== Iniciando Microservicios =====
start "ms-courses" mvn -f ms-courses spring-boot:run
start "ms-grades" mvn -f ms-grades spring-boot:run
start "ms-payments" mvn -f ms-payments spring-boot:run
start "ms-support" mvn -f ms-support spring-boot:run
start "ms-users" mvn -f ms-users spring-boot:run
rem agrega aquí los demás microservicios si necesitas

echo Todos los servicios han sido lanzados.
