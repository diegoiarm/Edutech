# 🎓 Sistema de gestión de clases de EduTech Innovators SPA

Sistema de gestión de cursos educativos basado en microservicios desarrollado con Spring Boot y Spring Cloud.

## 📁 Estructura del proyecto

```
Edutech/
├── 📁 common/                    # Módulo común con DTOs y utilidades compartidas
├── 📁 eureka/                    # Servidor de descubrimiento de servicios (puerto 8761)
├── 📁 ms-users/                  # Microservicio de usuarios y autenticación (puerto 9001)
├── 📁 ms-courses/                # Microservicio de cursos y contenido académico (puerto 9002)
├── 📁 ms-grades/                 # Microservicio de calificaciones y evaluaciones (puerto 9003)
├── 📁 ms-payments/               # Microservicio de pagos y transacciones (puerto 9004)
├── 📁 ms-support/                # Microservicio de soporte técnico (puerto 9005)
├── 📄 install.bat                # Script de instalación de dependencias
├── 📄 run-all.bat                # Script para iniciar todos los servicios
├── 📄 run-eureka.bat             # Script para iniciar solo Eureka
├── 📄 run-users.bat              # Script para iniciar solo el servicio de usuarios
├── 📄 run-courses.bat            # Script para iniciar solo el servicio de cursos
├── 📄 run-grades.bat             # Script para iniciar solo el servicio de calificaciones
├── 📄 run-payments.bat           # Script para iniciar solo el servicio de pagos
├── 📄 run-support.bat            # Script para iniciar solo el servicio de soporte
└── 📄 create-db.sql              # Script para crear la base de datos
```

## 🚀 Pasos para ejecutar

### 1. Prerrequisitos
- Java 21 o superior
- Maven 3.6+
- MySQL 8.0+

### 2. Instalar dependencias
```bash
# Ejecutar el script de instalación
install.bat
```

### 3. Configurar la base de datos
```bash
# Ejecutar el script SQL para crear la base de datos
mysql -u root -p < create-db.sql
```

### 4. Iniciar los microservicios

#### Opción A: Iniciar todos los servicios de una vez
```bash
run-all.bat
```

#### Opción B: Iniciar servicios individualmente (en orden recomendado)

1. **Iniciar Eureka Server** (servidor de descubrimiento):
```bash
run-eureka.bat
```

2. **Iniciar microservicios** (en cualquier orden):
```bash
run-users.bat
run-courses.bat
run-grades.bat
run-payments.bat
run-support.bat
```

### 5. Verificar que todo funciona

- **Eureka Dashboard**: http://localhost:8761
- **API Documentation**: Cada servicio expone su documentación Swagger en `/swagger-ui.html`

## 📋 Puertos de los Servicios

| Servicio | Puerto | Descripción |
|----------|--------|-------------|
| Eureka Server | 8761 | Servidor de descubrimiento |
| Users Service | 9001 | Gestión de usuarios y autenticación |
| Courses Service | 9002 | Gestión de cursos y contenido |
| Grades Service | 9003 | Gestión de calificaciones |
| Payments Service | 9004 | Gestión de pagos |
| Support Service | 9005 | Gestión de soporte técnico |

## 🔧 Configuración

Los archivos de configuración se encuentran en:
- `src/main/resources/application.yml` en cada microservicio
- `src/main/resources/application-dev.yml` para configuraciones de desarrollo

## 📚 Documentación de APIs

Una vez que los servicios estén ejecutándose, puedes acceder a la documentación de cada API en:
- Users API: http://localhost:9001/swagger-ui.html
- Courses API: http://localhost:9002/swagger-ui.html
- Grades API: http://localhost:9003/swagger-ui.html
- Payments API: http://localhost:9004/swagger-ui.html
- Support API: http://localhost:9005/swagger-ui.html

## 🛠️ Comandos Útiles

```bash
# Compilar todo el proyecto
mvn clean install

# Ejecutar tests
mvn test

# Limpiar y recompilar
mvn clean compile

# Ver logs de un servicio específico
# Los logs se muestran en la consola donde ejecutaste el script
```

## ⚠️ Notas Importantes

1. **Orden de inicio**: Es recomendable iniciar Eureka primero, aunque los microservicios se registrarán automáticamente cuando esté disponible.

2. **Base de datos**: Asegúrate de que MySQL esté ejecutándose antes de iniciar los servicios.

3. **Puertos**: Verifica que los puertos especificados no estén siendo utilizados por otras aplicaciones.


## 🐛 Algunos problemas comunes:

- **Error de conexión a la base de datos**: Verifica que MySQL esté ejecutándose y las credenciales sean correctas.
- **Puerto en uso**: Cambia el puerto en el archivo `application.yml` del servicio correspondiente.
- **Servicio no se registra en Eureka**: Espera unos segundos, el registro puede demorarse.

