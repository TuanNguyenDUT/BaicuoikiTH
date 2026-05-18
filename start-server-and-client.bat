@echo off
chcp 65001 >nul
cd /d "%~dp0"

echo ========================================
echo  TEST HỆ THỐNG: Server + Client cùng máy
echo ========================================
echo.
echo Terminal 1: Chạy Server trên port 5000
echo Terminal 2: Chạy Client kết nối đến localhost
echo.

REM Kiểm tra Maven
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Maven chưa được cài đặt
    pause
    exit /b 1
)

REM Compile trước
echo ⏳ Đang compile...
call mvn clean compile -q 2>nul

if %ERRORLEVEL% NEQ 0 (
    echo ❌ Lỗi compile!
    pause
    exit /b 1
)

echo ✅ Compile xong!
echo.

REM Chạy Server trên terminal 1
echo 🚀 Khởi động Server...
start "SERVER - Port 5000" cmd /k "cd /d "%~dp0" && mvn exec:java -Dexec.mainClass=server.ServerGUI"

REM Chờ server khởi động
timeout /t 3 /nobreak

REM Chạy Client trên terminal 2
echo 🚀 Khởi động Client...
start "CLIENT - Kết nối localhost:5000" cmd /k "cd /d "%~dp0" && mvn exec:java -Dexec.mainClass=client.ClientGUI"

echo.
echo ✅ Cả Server và Client đã khởi động!
echo.
echo Hướng dẫn:
echo 1. Chờ cả 2 cửa sổ GUI mở lên
echo 2. Trên Client GUI:
echo    - Server Host: localhost (hoặc 127.0.0.1)
echo    - Port: 5000
echo 3. Click "Kết nối" để test
echo.
echo Nhấn phím bất kỳ để đóng...
pause
