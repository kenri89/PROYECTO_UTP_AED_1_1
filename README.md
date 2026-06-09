# PROYECTO_UTP_AED_1

Sistema de Gestión Académica — Proyecto del curso Algoritmos y Estructuras de Datos (AED).

## Requisitos

- Java 21 o superior
- Microsoft SQL Server (local)
- NetBeans IDE (opcional)

## Configuración de Base de Datos

1. Crear la base de datos en SQL Server:

```sql
CREATE DATABASE iestp_peru_japon;
```

2. Ejecutar `schema.sql` para crear las tablas necesarias.

3. Editar `src/config.properties` con tus credenciales de BD:

```properties
db.server=localhost
db.port=1433
db.name=iestp_peru_japon
db.user=sa
db.password=tu_password
```

## Librerías incluidas

| Librería | Versión | Propósito |
|---|---|---|
| Google Guava | 33.4.0-jre | Utilidades generales |
| Apache Commons IO | 2.18.0 | Manejo de archivos |
| Apache POI | 5.4.0 | Exportación a Excel |
| Logback + SLF4J | 1.5.15 / 2.0.16 | Logging |
| JUnit Jupiter | 5.11.4 | Pruebas unitarias |
| mssql-jdbc | 13.4.0 | Conector SQL Server |

## Arquitectura

- **MVC**: `Modelo/` (modelo), `GUI/` (vistas), controladores en las GUI
- **DAO**: `dao/` — acceso a datos con `PreparedStatement`
- **Estructuras**: `ESTRUCTURAS/` — implementaciones propias de listas, pilas, árboles, matrices

## Ejecución

Desde NetBeans: abrir proyecto y ejecutar (Main class: `gui.Main`).

Desde terminal:

```bash
ant run
```

## Usuarios por defecto

| Usuario | Contraseña | Rol |
|---|---|---|
| admin | 1234 | Administrador |
| secretaria | 1234 | Secretaría |
| estudiante | 1234 | Estudiante |
