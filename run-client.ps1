#!/usr/bin/env powershell
# Script để chạy Client GUI

Write-Host ""
Write-Host "======================================"
Write-Host "Khoi dong CLIENT..."
Write-Host "======================================"
Write-Host ""
Write-Host "Nhap Server Host va Port trong GUI"
Write-Host "======================================"
Write-Host ""

Set-Location "E:\python\LapTrinhMang\BaicuoikiTH"

# Build project trước (nếu chưa có)
Write-Host "Dang build project..."
& cmd /c "mvn clean install -q"

Write-Host "Dang chay Client..."
Write-Host ""

# Chạy Client với Maven
& cmd /c "mvn exec:java -q -Dexec.mainClass=client.ClientGUI"

Write-Host ""
Write-Host "======================================"
Write-Host "Client da dong"
Write-Host "======================================"
Write-Host ""
