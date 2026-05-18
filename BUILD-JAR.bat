@echo off
REM ========================================
REM  Build Project & Tạo Uber JAR
REM  Bạn tôi chỉ cần chạy JAR này, không cần Maven
REM ========================================
chcp 65001 >nul

echo.
echo ========================================
echo  BUILD PROJECT - Tạo JAR Standalone
echo ========================================
echo.

REM Kiểm tra Maven
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Maven chưa được cài đặt
    echo.
    echo Vui lòng cài Maven hoặc chạy từ terminal có Maven
    pause
    exit /b 1
)

echo ⏳ Bắt đầu build...
echo.

REM Clean & Build
echo 🧹 Cleaning...
call mvn clean -q

echo 📦 Compiling...
call mvn compile -q

echo 🔨 Packaging...
call mvn package -DskipTests -q

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ❌ Build FAILED!
    pause
    exit /b 1
)

echo.
echo ✅ BUILD THÀNH CÔNG!
echo.
echo 📁 File JAR được tạo:
echo    - target\exam-server-all.jar (Uber JAR - chứa tất cả)
echo.
echo 📦 Để share cho bạn:
echo    1. Copy file: target\exam-server-all.jar
echo    2. Copy file: RUN-SERVER-STANDALONE.bat
echo    3. Copy file: RUN-CLIENT-STANDALONE.bat
echo    4. Zip lại (bỏ thêm file input/output nếu muốn)
echo    5. Gửi cho bạn
echo.
echo 🚀 Bạn tôi chỉ cần:
echo    - Tải Java Runtime (JRE) từ java.com
echo    - Double click RUN-SERVER-STANDALONE.bat hoặc RUN-CLIENT-STANDALONE.bat
echo    - Chỉ vậy thôi! Không cần Maven, JDK hay gì cả!
echo.
echo 💾 Tạo distribution package:
echo    call mvn assembly:single -DskipTests -q
echo.
pause
