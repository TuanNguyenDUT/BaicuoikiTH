@REM Maven Wrapper - Tự tải Maven nếu chưa có
@REM Bạn chỉ cần chạy: mvnw.cmd clean compile package -DskipTests

@echo off
setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%

@REM Tìm Java
where java >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Error: Java is not installed or not in PATH
    echo Please install JDK from: https://www.oracle.com/java/
    echo.
    pause
    exit /b 1
)

@REM Tạo thư mục .mvn nếu chưa có
if not exist ".mvn" mkdir .mvn
if not exist ".mvn\wrapper" mkdir .mvn\wrapper

@REM Download Maven Wrapper nếu chưa có
if not exist ".mvn\wrapper\maven-wrapper.jar" (
    echo Downloading Maven Wrapper...
    powershell -Command "(New-Object System.Net.ServicePointManager).SecurityProtocol = [System.Net.SecurityProtocolType]::Tls12; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar' -OutFile '.mvn\wrapper\maven-wrapper.jar'"
)

@REM Download maven-wrapper.properties nếu chưa có
if not exist ".mvn\wrapper\maven-wrapper.properties" (
    (
        echo distributionUrl=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.1/apache-maven-3.9.1-bin.zip
        echo wrapperUrl=https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar
    ) > ".mvn\wrapper\maven-wrapper.properties"
)

@REM Chạy Maven Wrapper
java -cp ".mvn\wrapper\maven-wrapper.jar" org.apache.maven.wrapper.MavenWrapperMain %*
