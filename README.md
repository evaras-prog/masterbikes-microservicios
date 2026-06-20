# MasterBikes Microservicios

Sistema de gestión de usuarios, productos e inventario para la empresa MasterBikes, desarrollado con arquitectura de microservicios usando Spring Boot.

---

## Integrantes y aportes

| Integrante | Aporte |
|---|---|
| Andrea Huenumilla | Desarrollo de productos-inventario-api: modelos, repositorios, servicios y controladores |
| Rosa Medina | Desarrollo de usuarios-clientes-api: modelos, repositorios, servicios y controladores |
| Mario González | Configuración de seguridad JWT, filtros y autenticación |
| Esteban Varas | API Gateway, pruebas unitarias con Mockito, documentación Swagger y configuración general |

---

## Arquitectura

```
Cliente (Postman/Swagger)
        │
        ▼
  API Gateway (:8080)
   /api/v1/usuarios  ──►  usuarios-clientes-api (:8081)
   /api/v1/auth      ──►  usuarios-clientes-api (:8081)
   /api/v1/productos ──►  productos-inventario-api (:8082)
   /api/v1/categorias──►  productos-inventario-api (:8082)
```

---

## Microservicios

### usuarios-clientes-api — Puerto 8081

| Método | Endpoint | Descripción |
|---|---|---|
| POST | /api/v1/auth/login | Iniciar sesión |
| GET | /api/v1/usuarios | Listar todos los usuarios |
| GET | /api/v1/usuarios/{id} | Obtener usuario por ID |
| POST | /api/v1/usuarios | Registrar nuevo usuario |
| PUT | /api/v1/usuarios/{id} | Actualizar usuario |
| DELETE | /api/v1/usuarios/{id} | Eliminar usuario |
| GET | /api/v1/usuarios/{id}/productos/{idProducto} | Consultar producto desde usuarios-api |

### productos-inventario-api — Puerto 8082

| Método | Endpoint | Descripción |
|---|---|---|
| GET | /api/v1/productos | Listar todos los productos |
| GET | /api/v1/productos/{id} | Obtener producto por ID |
| POST | /api/v1/productos | Registrar nuevo producto |
| PUT | /api/v1/productos/{id} | Actualizar producto |
| DELETE | /api/v1/productos/{id} | Eliminar producto |
| GET | /api/v1/categorias | Listar categorías |
| GET | /api/v1/categorias/{id} | Obtener categoría por ID |
| POST | /api/v1/categorias | Registrar nueva categoría |
| PUT | /api/v1/categorias/{id} | Actualizar categoría |
| DELETE | /api/v1/categorias/{id} | Eliminar categoría |
| GET | /api/v1/productos/{id}/usuarios/{idUsuario} | Consultar usuario desde productos-api |

---

## API Gateway — Puerto 8080

El Gateway centraliza el acceso a los microservicios. Todas las peticiones pueden realizarse a través del puerto 8080:

| Prefijo | Redirige a |
|---|---|
| /api/v1/usuarios/** | http://localhost:8081 |
| /api/v1/auth/** | http://localhost:8081 |
| /api/v1/productos/** | http://localhost:8082 |
| /api/v1/categorias/** | http://localhost:8082 |

---

## Swagger / OpenAPI

| Microservicio | URL Swagger |
|---|---|
| usuarios-clientes-api | http://localhost:8081/doc/swagger-ui/index.html |
| productos-inventario-api | http://localhost:8082/doc/swagger-ui/index.html |

---

## Instrucciones para ejecutar el sistema

### Requisitos previos
- Java 17+
- Maven
- Oracle Autonomous Database (Wallet configurada)

### Pasos

1. Configurar la Wallet de Oracle en ambos `application.properties`:
   ```
   spring.datasource.url=jdbc:oracle:thin:@hospital_high?TNS_ADMIN=C:/Wallet_HOSPITAL
   spring.datasource.username=ADMIN
   spring.datasource.password=FullStack-001
   ```

2. Levantar los microservicios en este orden:
   ```bash
   # Terminal 1
   cd usuarios-clientes-api
   mvn spring-boot:run

   # Terminal 2
   cd productos-inventario-api
   mvn spring-boot:run

   # Terminal 3
   cd api-gateway
   mvn spring-boot:run
   ```

3. Verificar que los tres servicios están activos:
   - Gateway: http://localhost:8080
   - Usuarios: http://localhost:8081
   - Productos: http://localhost:8082

4. Probar los endpoints desde Swagger o Postman. Para endpoints protegidos, primero obtener el token JWT desde `/api/v1/auth/login`.

### Ejecutar pruebas unitarias
```bash
cd usuarios-clientes-api
mvn test

cd productos-inventario-api
mvn test
```

---

## Tecnologías utilizadas

- Java 17
- Spring Boot 3
- Spring Security + JWT
- Spring Data JPA
- OpenFeign (comunicación entre microservicios)
- Oracle Autonomous Database
- Springdoc OpenAPI (Swagger)
- JUnit 5 + Mockito (pruebas unitarias)
- Maven
