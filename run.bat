@echo off
if not exist "bin\com\miva\coursemanagement\Main.class" (
    echo Binaries not found. Building first...
    call build.bat
    if %ERRORLEVEL% NEQ 0 exit /b %ERRORLEVEL%
)

echo Starting Student Course Management System...
java -cp bin com.miva.coursemanagement.Main

