# MIVA Course Management System - GUI Launcher

$ErrorActionPreference = "Stop"

if (-not (Test-Path "bin\com\miva\coursemanagement\CourseManagementGUI.class")) {
    Write-Host "Compiling source files..." -ForegroundColor Yellow
    if (-not (Test-Path "bin")) { New-Item -ItemType Directory -Path "bin" | Out-Null }
    javac -d bin -sourcepath src src/com/miva/coursemanagement/*.java
    if ($LASTEXITCODE -ne 0) {
        Write-Host "[ERROR] Compilation failed." -ForegroundColor Red
        exit 1
    }
}

Write-Host "Launching Graphical User Interface (GUI)..." -ForegroundColor Green
Start-Process "javaw" -ArgumentList "-cp bin com.miva.coursemanagement.CourseManagementGUI"

