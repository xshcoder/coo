@echo off
cd /d "%~dp0coo-web"
echo Building and starting coo-web...
call docker compose up --build -d
cd ..