@echo off
chcp 65001 >nul
echo Khoi dong server (yeu cau da build truoc)...
echo Truy cap: http://localhost:8080
echo.
cd /d "%~dp0..\backend"
call mvn cargo:run
pause
