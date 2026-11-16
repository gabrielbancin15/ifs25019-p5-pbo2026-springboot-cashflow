@echo off
where mvn >nul 2>nul
if %ERRORLEVEL% neq 0 (echo Maven belum terpasang. Install Maven 3.9+ atau gunakan IntelliJ IDEA. & exit /b 1)
mvn %*
