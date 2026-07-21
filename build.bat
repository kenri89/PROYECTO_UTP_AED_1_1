@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion

echo ========================================
echo  GENERANDO EJECUTABLE DEL SISTEMA
echo ========================================
echo.

REM ========================================
REM CONFIGURACION DEL JDK 25
REM ========================================

set "JAVA_HOME=C:\Program Files\Java\jdk-25.0.3"
set "JAVAC=%JAVA_HOME%\bin\javac.exe"
set "JAR=%JAVA_HOME%\bin\jar.exe"
set "JPACKAGE=%JAVA_HOME%\bin\jpackage.exe"

if not exist "%JPACKAGE%" (
    echo ERROR: No se encuentra jpackage
    echo Ruta buscada:
    echo %JPACKAGE%
    pause
    exit /b 1
)

echo JDK utilizado:
echo %JAVA_HOME%
echo.


REM ========================================
REM COMPILACION DEL PROYECTO
REM ========================================

echo 1. Compilando proyecto...

if exist "build\classes" (
    rmdir /s /q "build\classes"
)

mkdir "build\classes" 2>nul


REM Crear lista de librerias externas

set "LIBS="

for %%j in ("dist\lib\*.jar") do (
    if defined LIBS (
        set "LIBS=!LIBS!;%%j"
    ) else (
        set "LIBS=%%j"
    )
)


REM Buscar archivos JAVA

set "SOURCES="

for /r "src" %%f in (*.java) do (
    if defined SOURCES (
        set "SOURCES=!SOURCES! "%%f""
    ) else (
        set "SOURCES="%%f""
    )
)


REM Compilar usando Java 21

"%JAVAC%" ^
-d "build\classes" ^
-cp "!LIBS!" ^
-encoding UTF-8 ^
--release 21 ^
!SOURCES!


if %errorlevel% neq 0 (
    echo.
    echo ERROR: Fallo la compilacion
    pause
    exit /b 1
)


echo Compilacion correcta.
echo.


REM ========================================
REM CREAR ARCHIVO JAR
REM ========================================

echo 2. Generando archivo JAR...


if exist "dist\PROYECTO_UTP_AED_1.jar" (
    del /q "dist\PROYECTO_UTP_AED_1.jar"
)


"%JAR%" ^
cfe "dist\PROYECTO_UTP_AED_1.jar" ^
gui.Main ^
-C "build\classes" . ^
-C "src" gui\logo.png


if %errorlevel% neq 0 (
    echo ERROR creando JAR
    pause
    exit /b 1
)


echo JAR generado correctamente.
echo.


REM ========================================
REM PREPARAR ARCHIVOS PARA JPACKAGE
REM ========================================

set "TEMP_INPUT=%TEMP%\SistemaAcademico-input"
set "TEMP_OUTPUT=%USERPROFILE%\Desktop\SistemaAcademico"


if exist "%TEMP_INPUT%" (
    rmdir /s /q "%TEMP_INPUT%"
)

if exist "%TEMP_OUTPUT%" (
    rmdir /s /q "%TEMP_OUTPUT%"
)


mkdir "%TEMP_INPUT%\lib"


copy "dist\PROYECTO_UTP_AED_1.jar" "%TEMP_INPUT%" >nul

copy "dist\lib\*.jar" "%TEMP_INPUT%\lib\" >nul


echo Archivos preparados.
echo.


REM ========================================
REM GENERAR APLICACION CON JPACKAGE
REM ========================================

echo 3. Generando ejecutable con JPACKAGE...
echo.


"%JPACKAGE%" ^
--type app-image ^
--input "%TEMP_INPUT%" ^
--name "SistemaAcademico" ^
--main-jar PROYECTO_UTP_AED_1.jar ^
--main-class gui.Main ^
--dest "%TEMP_OUTPUT%"


if %errorlevel% equ 0 (

    echo.
    echo ========================================
    echo          PROCESO FINALIZADO
    echo ========================================
    echo.
    echo Ejecutable generado:
    echo.
    echo %TEMP_OUTPUT%\SistemaAcademico\SistemaAcademico.exe
    echo.
    echo Incluye JRE propio.
    echo No requiere instalar Java.
    echo.

) else (

    echo.
    echo ERROR generando ejecutable.
    echo Codigo: %errorlevel%

)


pause