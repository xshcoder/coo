@echo off
cd /d "%~dp0coo-web"
echo starting coo-web
call docker compose up -d
cd ..