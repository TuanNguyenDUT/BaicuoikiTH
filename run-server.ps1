#!/usr/bin/env powershell
# Script để chạy Server GUI

Write-Host ""
Write-Host "======================================"
Write-Host "Khoi dong SERVER..."
Write-Host "======================================"
Write-Host ""
Write-Host "Server se chay tren port 5000"
Write-Host "Trang thai: Cho client ket noi..."
Write-Host ""

Set-Location "E:\python\LapTrinhMang\BaicuoikiTH"

# Build project trước (nếu chưa có)
Write-Host "Dang build project..."
& cmd /c "mvn clean install -q"

Write-Host "Dang chay Server..."
Write-Host ""

# Chạy Server với Maven
& cmd /c "mvn exec:java -q -Dexec.mainClass=server.ServerGUI"

Write-Host ""
Write-Host "======================================"
Write-Host "Server da dong"
Write-Host "======================================"
Write-Host ""
