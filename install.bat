@echo off
echo.
echo === REINSTALACION DE DEPENDENCIAS MAVEN ===
echo.

REM Paso 1: Eliminar carpeta local de dependencias
echo Eliminando carpeta .m2 ...
rmdir /s /q %USERPROFILE%\.m2

REM Paso 2: Eliminar carpetas target de los proyectos
echo Eliminando carpetas target ...
rmdir /s /q C:\classroom\common\target
rmdir /s /q C:\classroom\eureka\target
rmdir /s /q C:\classroom\ms-courses\target
rmdir /s /q C:\classroom\ms-grades\target
rmdir /s /q C:\classroom\ms-payments\target
rmdir /s /q C:\classroom\ms-support\target
rmdir /s /q C:\classroom\ms-users\target

REM Paso 3: Instalar todas las dependencias forzadamente
echo Descargando dependencias nuevamente con Maven ...
mvn clean install -U -DskipTests

echo.
echo === PROCESO COMPLETADO ===
pause
