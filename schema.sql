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
    semestre INT NOT NULL
);
GO

CREATE TABLE Estudiantes (
    carnet VARCHAR(20) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    carrera VARCHAR(100) NOT NULL
);
GO

-- Usuario administrador por defecto
INSERT INTO usuarios (username, password, rol) VALUES ('admin', '1234', 'Administrador');
INSERT INTO usuarios (username, password, rol) VALUES ('secretaria', '1234', 'Secretaría');
GO
