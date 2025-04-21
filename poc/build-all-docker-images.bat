@echo off
echo Building user-service...
cd user-service
call docker build -t user-service:latest .
cd ..

echo Building coo-service...
cd coo-service
call docker build -t coo-service:latest .
cd ..

echo Building reply-service...
cd reply-service
call docker build -t reply-service:latest .
cd ..

echo Building like-service...
cd like-service
call docker build -t like-service:latest .
cd ..

echo Building follow-service...
cd follow-service
call docker build -t follow-service:latest .
cd ..

echo Building search-service...
cd search-service
call docker build -t search-service:latest .
cd ..

echo Building postgres-service...
cd postgres
call docker build -t postgres-service:latest .
cd ..

echo Building personalize-service...
cd personalize
call docker build -t personalize-service:latest .
cd ..

echo All images built successfully!
pause