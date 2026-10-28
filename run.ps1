# COS 201 Course Management System PowerShell Runner

$ErrorActionPreference = "Stop"

Write-Host "===================================================" -ForegroundColor Cyan
Write-Host "Building and Running Course Management System..." -ForegroundColor Cyan
Write-Host "===================================================" -ForegroundColor Cyan

if (-not (Test-Path "bin")) {
    New-Item -ItemType Directory -Path "bin" | Out-Null
}

Write-Host "Compiling source files..." -ForegroundColor Yellow
javac -d bin -sourcepath src src/com/miva/coursemanagement/*.java

if ($LASTEXITCODE -eq 0) {
    Write-Host "[SUCCESS] Compilation successful! Launching program...`n" -ForegroundColor Green
    java -cp bin com.miva.coursemanagement.Main
} else {
    Write-Host "[ERROR] Compilation failed." -ForegroundColor Red
}

