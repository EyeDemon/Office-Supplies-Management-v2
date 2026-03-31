@echo off
chcp 65001 >nul
echo ================================================
echo   Cai dat Database
echo ================================================
set /p MYSQL_USER="MySQL username (mac dinh root): "
if "%MYSQL_USER%"=="" set MYSQL_USER=root
set /p MYSQL_PASS="MySQL password: "
echo.
echo [1/2] Tao bang...
mysql -u%MYSQL_USER% -p%MYSQL_PASS% < "%~dp0..\database\schema.sql"
if errorlevel 1 (echo [LOI] schema.sql that bai. & pause & exit /b 1)
echo [2/2] Them du lieu mau...
mysql -u%MYSQL_USER% -p%MYSQL_PASS% < "%~dp0..\database\data.sql"
if errorlevel 1 (echo [LOI] data.sql that bai. & pause & exit /b 1)
echo.
echo Hoan tat! Tai khoan: admin/manager/nhanvien1..8, mat khau: 123456
pause
