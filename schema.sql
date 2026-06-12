-- Esquema de base de datos para PROYECTO_UTP_AED_1
-- Ejecutar contra SQL Server

CREATE DATABASE iestp_peru_japon;
GO

USE iestp_peru_japon;
GO

CREATE TABLE usuarios (
    id INT IDENTITY(1,1) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    rol VARCHAR(20) NOT NULL DEFAULT 'Administrador'
);
GO

CREATE TABLE Cursos (
    codigo VARCHAR(20) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    creditos INT NOT NULL,
    semestre INT NOT NULL
);
GO

CREATE TABLE Estudiantes (
    carnet VARCHAR(20) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    carrera VARCHAR(100) NOT NULL,
    username VARCHAR(50) NULL
);
GO

CREATE TABLE Matriculas (
    id INT IDENTITY(1,1) PRIMARY KEY,
    carnet VARCHAR(20) NOT NULL,
    codigo_curso VARCHAR(20) NOT NULL,
    fecha_registro DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (carnet) REFERENCES Estudiantes(carnet),
    FOREIGN KEY (codigo_curso) REFERENCES Cursos(codigo)
);
GO

CREATE TABLE Solicitudes (
    id_solicitud INT IDENTITY(1,1) PRIMARY KEY,
    carnet_estudiante VARCHAR(20) NOT NULL,
    descripcion VARCHAR(500),
    fecha_creacion DATETIME DEFAULT GETDATE(),
    atendida BIT DEFAULT 0,
    fecha_atencion DATETIME NULL,
    FOREIGN KEY (carnet_estudiante) REFERENCES Estudiantes(carnet)
);
GO

-- Usuarios por defecto
INSERT INTO usuarios (username, password, rol) VALUES ('admin', '1234', 'Administrador');
INSERT INTO usuarios (username, password, rol) VALUES ('secretaria', '1234', 'Secretaria');
INSERT INTO usuarios (username, password, rol) VALUES ('estudiante', '1234', 'Estudiante');
GO
