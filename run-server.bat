@echo off
REM Script để chạy Server
REM Chạy: run-server.bat

echo.
echo ======================================
echo Khoi dong SERVER...
echo ======================================
echo.
echo Server se chay tren port 5000
echo Trang thai: Cho client ket noi...
echo.

cd /d "%~dp0"

java -jar target\exam-server-all.jar

echo.
echo ======================================
echo Server da dong
echo ======================================
echo.

pause
