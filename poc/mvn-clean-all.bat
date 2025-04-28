@echo off
echo Cleaning user-service...
cd user-service
call mvn clean
cd ..

echo Cleaning coo-service...
cd coo-service
call mvn clean
cd ..

echo Cleaning reply-service...
cd reply-service
call mvn clean
cd ..

echo Cleaning like-service...
cd like-service
call mvn clean
cd ..

echo Cleaning follow-service...
cd follow-service
call mvn clean
cd ..

echo Cleaning search-service...
cd search-service
call mvn clean
cd ..

echo Cleaning personalize-service...
cd personalize-service
call mvn clean
cd ..

echo Cleaning statistics-service...
cd statistics-service
call mvn clean
cd ..

echo All services cleaned!
pause