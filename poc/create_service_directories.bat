@echo off
setlocal

REM Check if service name is provided
if "%~1"=="" (
    echo Usage: %~nx0 [service-name]
    echo Example: %~nx0 coo
    exit /b 1
)

set SERVICE_NAME=%~1
set BASE_PATH=%cd%\%SERVICE_NAME%-service

echo Creating directories for %SERVICE_NAME%-service in:
echo %BASE_PATH%

REM Create main directory structure
mkdir "%BASE_PATH%\src\main\java\com\xsh\%SERVICE_NAME%"
mkdir "%BASE_PATH%\src\main\resources"
mkdir "%BASE_PATH%\src\test\java\com\xsh\%SERVICE_NAME%"
mkdir "%BASE_PATH%\src\test\resources"

REM Create standard Spring Boot package structure
mkdir "%BASE_PATH%\src\main\java\com\xsh\%SERVICE_NAME%\controller"
mkdir "%BASE_PATH%\src\main\java\com\xsh\%SERVICE_NAME%\service"
mkdir "%BASE_PATH%\src\main\java\com\xsh\%SERVICE_NAME%\repository"
mkdir "%BASE_PATH%\src\main\java\com\xsh\%SERVICE_NAME%\model"
mkdir "%BASE_PATH%\src\main\java\com\xsh\%SERVICE_NAME%\config"

echo Directory structure for %SERVICE_NAME%-service created successfully!
