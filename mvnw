#!/bin/bash
# Maven Wrapper - Tự tải Maven nếu chưa có
# Bạn chỉ cần chạy: ./mvnw clean compile package -DskipTests

DIRNAME=$(cd "$(dirname "$0")" && pwd)
APP_BASE_NAME=$(basename "$0")
APP_HOME="$DIRNAME"

# Tìm Java
if ! command -v java &> /dev/null; then
    echo ""
    echo "Error: Java is not installed or not in PATH"
    echo "Please install JDK from: https://www.oracle.com/java/"
    echo ""
    exit 1
fi

# Tạo thư mục .mvn nếu chưa có
mkdir -p "$APP_HOME/.mvn/wrapper"

# Download Maven Wrapper nếu chưa có
if [ ! -f "$APP_HOME/.mvn/wrapper/maven-wrapper.jar" ]; then
    echo "Downloading Maven Wrapper..."
    curl -fsSL -o "$APP_HOME/.mvn/wrapper/maven-wrapper.jar" \
        "https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"
fi

# Download maven-wrapper.properties nếu chưa có
if [ ! -f "$APP_HOME/.mvn/wrapper/maven-wrapper.properties" ]; then
    cat > "$APP_HOME/.mvn/wrapper/maven-wrapper.properties" << EOF
distributionUrl=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.1/apache-maven-3.9.1-bin.zip
wrapperUrl=https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar
EOF
fi

# Chạy Maven Wrapper
java -cp "$APP_HOME/.mvn/wrapper/maven-wrapper.jar" org.apache.maven.wrapper.MavenWrapperMain "$@"
