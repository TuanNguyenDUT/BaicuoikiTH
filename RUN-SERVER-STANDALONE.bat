@echo off
REM ========================================
REM  Chạy Server - Phân công giám thị coi thi
REM  Chỉ cần cài Java Runtime (JRE), không cần JDK
REM ========================================
chcp 65001 >nul

setlocal enabledelayedexpansion

REM Tìm Java trong system
where java >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ❌ LỖI: Java chưa được cài đặt hoặc chưa thêm vào PATH
    echo.
    echo 💡 Cách khắc phục:
    echo    1. Tải Java Runtime (JRE) từ: https://www.java.com/
    echo    2. Cài đặt Java
    echo    3. Chạy lại file này
    echo.
    pause
    exit /b 1
)

REM Lấy phiên bản Java
for /f "tokens=3" %%g in ('java -version 2^>^&1 ^| findstr /i "version"') do (
    set JAVA_VERSION=%%g
)

echo ========================================
echo  ✅ Java Runtime đã cài: !JAVA_VERSION!
echo ========================================
echo.

REM Kiểm tra file JAR
if not exist "exam-server-all.jar" (
    echo ❌ Lỗi: exam-server-all.jar không tìm thấy!
    echo.
    echo Vui lòng tải lại bộ cài đầy đủ
    echo.
    pause
    exit /b 1
)

echo 🚀 Khởi động Server...
echo.
echo ℹ️  Server sẽ lắng nghe trên PORT 5000
echo ℹ️  Cửa sổ GUI sẽ mở trong vài giây...
echo.

REM Chạy JAR file
java -jar exam-server-all.jar

echo.
echo ⚠️  Server đã dừng
pause
