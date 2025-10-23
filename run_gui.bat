@echo off
setlocal
set ROOT=%~dp0
if not exist "%ROOT%out" mkdir "%ROOT%out"
javac -d "%ROOT%out" -sourcepath "%ROOT%src" "%ROOT%src\com\rental\MainGui.java"
if errorlevel 1 (
  echo Build failed.
  pause
  exit /b 1
)
java -cp "%ROOT%out" com.rental.MainGui
pause
