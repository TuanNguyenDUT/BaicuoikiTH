@echo off
REM Script để chạy Client
REM Chạy: run-client.bat

echo.
echo ======================================
echo Khoi dong CLIENT...
echo ======================================
echo.
echo Nhap Server Host va Port trong GUI
echo ======================================
echo.

cd /d "%~dp0"

java -cp target\exam-server-all.jar client.ClientGUI

echo.
echo ======================================
echo Client da dong
echo ======================================
echo.

pause
