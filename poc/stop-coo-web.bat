@echo off
cd /d "%~dp0coo-web"
echo Stopping coo-web service...
call docker compose stop coo-web
cd ..