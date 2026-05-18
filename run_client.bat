@echo off
REM Compile and Run FileShareClient

cd /d "%~dp0"

echo.
echo ============================
echo FileShare Client Compiler
echo ============================
echo.

REM Check if javac is available
where javac >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo Error: Java JDK not found. Please install JDK first.
    echo.
    echo Download from: https://www.oracle.com/java/technologies/downloads/
    pause
    exit /b 1
)

echo.
echo Compiling FileShareClient.java...
javac FileShareClient.java

if %ERRORLEVEL% EQU 0 (
    echo Compilation successful!
    echo.
    echo ============================
    echo Starting FileShare Client...
    echo ============================
    echo.
    java FileShareClient
) else (
    echo Compilation failed!
    pause
)
