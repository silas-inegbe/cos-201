@echo off
if not exist "bin\com\miva\coursemanagement\CourseManagementGUI.class" (
    echo Compiling application...
    call build.bat
    if %ERRORLEVEL% NEQ 0 exit /b %ERRORLEVEL%
)

echo Starting MIVA Student Course Management GUI...
start javaw -cp bin com.miva.coursemanagement.CourseManagementGUI

