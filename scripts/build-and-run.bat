@echo off
chcp 65001 >nul
echo ================================================
echo   Quan Ly Van Phong Pham - Build and Run
echo ================================================
echo.

java -version >nul 2>&1 || (echo [LOI] Chua cai Java 17+. & pause & exit /b 1)
mvn -version  >nul 2>&1 || (echo [LOI] Chua cai Maven 3.8+. & pause & exit /b 1)

echo Truoc khi chay, dam bao:
echo   1. MySQL dang chay
echo   2. Da chay database\schema.sql va database\data.sql
echo   3. Da cap nhat mat khau trong backend\src\main\resources\application.properties
echo.
set /p confirm="Da san sang? (y/n): "
if /i not "%confirm%"=="y" (echo Thoat. & pause & exit /b 0)

echo.
echo [1/2] Building... (lan dau co the mat 3-5 phut de tai Node.js)
cd /d "%~dp0..\backend"
call mvn clean package -DskipTests
if errorlevel 1 (echo. & echo [LOI] Build that bai. & pause & exit /b 1)

echo.
echo [2/2] Starting server...
echo ================================================
echo   http://localhost:8080
echo   Dang nhap: admin / 123456
echo   Ctrl+C de dung
echo ================================================
call mvn cargo:run
pause
