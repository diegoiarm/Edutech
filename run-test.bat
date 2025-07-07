@echo off
echo ===== Iniciando Eureka Server =====
start "EUREKA" java -jar eureka\target\eureka.jar --spring.profiles.active=test

timeout /t 5 /nobreak > nul

echo ===== Iniciando Microservicios =====
start "MS-USERS"     java -jar ms-users\target\ms-users.jar --spring.profiles.active=test
start "MS-COURSES"   java -jar ms-courses\target\ms-courses.jar --spring.profiles.active=test
start "MS-PAYMENTS"  java -jar ms-payments\target\ms-payments.jar --spring.profiles.active=test
start "MS-ORDERS"    java -jar ms-orders\target\ms-orders.jar --spring.profiles.active=test
rem agrega aquí los demás microservicios si necesitas

echo Todos los servicios han sido lanzados.
