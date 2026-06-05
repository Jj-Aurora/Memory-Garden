@echo off
setlocal enabledelayedexpansion

set SCRIPT_DIR=%~dp0

REM Load environment variables from .env
if exist "%SCRIPT_DIR%.env" (
    echo [INFO] Loading .env file...
    for /f "usebackq tokens=1,* delims==" %%a in ("%SCRIPT_DIR%.env") do (
        set "line=%%a"
        if not "!line:~0,1!"=="#" if not "%%a"=="" set "%%a=%%b"
    )
) else (
    echo [WARN] .env file not found, using defaults or system environment variables
)

REM Required checks
set MISSING=0
if "%JWT_SECRET%"=="" (
    echo [ERROR] JWT_SECRET is required
    set MISSING=1
)
if "%DB_PASSWORD%"=="" (
    echo [ERROR] DB_PASSWORD is required
    set MISSING=1
)
if %MISSING%==1 (
    echo.
    echo Copy .env.example to .env and fill in the values:
    echo   copy "%SCRIPT_DIR%.env.example" "%SCRIPT_DIR%.env"
    exit /b 1
)

REM Defaults
if "%SERVER_PORT%"=="" set SERVER_PORT=8080
if "%DB_URL%"=="" set DB_URL=jdbc:mysql://localhost:3306/memory_garden?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
if "%DB_USERNAME%"=="" set DB_USERNAME=root

echo [INFO] Starting Memory Garden on port %SERVER_PORT%...

java -Xms256m -Xmx512m -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=prod -Dserver.port=%SERVER_PORT% -Dspring.config.additional-location="file:%SCRIPT_DIR%config/" -DJWT_SECRET="%JWT_SECRET%" -DDB_URL="%DB_URL%" -DDB_USERNAME="%DB_USERNAME%" -DDB_PASSWORD="%DB_PASSWORD%" -jar "%SCRIPT_DIR%backend\memory-garden-server-1.0.0.jar"
