@echo off
echo ===================================================
echo Compiling COS201 Student Course Management System...
echo ===================================================

if not exist "bin" mkdir bin

javac -d bin -sourcepath src src\com\miva\coursemanagement\*.java

if %ERRORLEVEL% EQU 0 (
    echo [SUCCESS] Compilation finished successfully. Class files stored in bin\
) else (
    echo [ERROR] Compilation failed. Please check the error messages above.
)

