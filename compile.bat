@echo off
setlocal
cd /d "%~dp0"

if not exist build mkdir build
if not exist data mkdir data

echo Compiling Online Bookstore...
dir /s /b src\bookstore\*.java > sources.txt
javac -d build -encoding UTF-8 @sources.txt
if errorlevel 1 (
    echo Compilation failed.
    del sources.txt 2>nul
    exit /b 1
)

del sources.txt 2>nul
echo Build successful. Run: run.bat
exit /b 0
