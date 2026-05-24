@echo off
cd /d "%~dp0"
if not exist build\bookstore\Main.class (
    call compile.bat
    if errorlevel 1 exit /b 1
)
java -cp build bookstore.Main
