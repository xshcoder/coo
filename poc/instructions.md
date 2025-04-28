# PostgreSQL and Spring Boot Microservice in Kubernetes

This project demonstrates how to run a PostgreSQL database and Spring Boot microservices in Kubernetes using Docker Desktop.

## Project Structure

- `k8s/` - Kubernetes manifests for deploying PostgreSQL and the Spring Boot microservices
- `postgres/` - PostgreSQL initialization scripts and Dockerfile
- `user-service/` - Spring Boot microservice for user management
- `coo-service/` - Spring Boot microservice for coo management (similar to tweets)
- `reply-service/` - Spring Boot microservice for managing replies to coos
- `like-service/` - Spring Boot microservice for managing likes to coos or replies
- `follow-service/` - Spring Boot microservice for managing user follows
- `search-service/` - Spring Boot microservice for searching users and coos
- `personalize-service/` - Spring Boot microservice for personalizing data for users
- `statistics-service/` - Spring Boot microservice for collecting statistics about coos and replies

## Prerequisites

- Docker Desktop with Kubernetes enabled
- kubectl CLI
- Java 17 or later
- Maven

## Getting Started

Follow the instructions in this README to build and deploy the application.

### build user service docker image
- docker build -t user-service:latest .

### build coo service docker image
- docker build -t coo-service:latest .

### build reply service docker image
- docker build -t reply-service:latest .

### build postgres service docker image
- docker build -t postgres-service:latest .

### build follow service docker image
- docker build -t follow-service:latest .

### build search service docker image
- docker build -t search-service:latest .

### build personalize service docker image
- docker build -t personalize-service:latest .

### build statistics service docker image
- docker build -t statistics-service:latest .

### deploy postgres service & user service
- deploy.bat

### undeploy postgres service & user service
- undeploy.bat

### user service swagger url
- http://localhost/swagger-ui/index.html

### coo service swagger url
- http://localhost/swagger-ui/index.html

### reply service swagger url
- http://localhost/swagger-ui/index.html

### follow service swagger url
- http://localhost/swagger-ui/index.html

### search service swagger url
- http://localhost/swagger-ui/index.html

### personalize service swagger url
- http://localhost/personalize/swagger-ui/index.html

### statistics service swagger url
- http://localhost/statistics/swagger-ui/index.html

### install ingress controller
- kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.12.1/deploy/static/provider/cloud/deploy.yaml

### install ingress
- kubectl apply -f ingress.yaml

### view yaml of ingress controller
- kubectl get svc ingress-nginx-controller -n ingress-nginx -o yaml

### view yaml of ingress
- kubectl get ingress coo-ingress -o yaml

### postgres database Port Forwarding (For Development)
>kubectl port-forward svc/postgres-service 5432:5432

### user service port forwarding
>kubectl port-forward svc/user-service 8080:80
http://localhost:8080/swagger-ui/index.html

### call coo service
http://localhost/api/coos

### kubenetes configuration folder
C:\Users\<you>\.kube
C:\Users\<you>\.kube\config

### init terraform
go to terraform folder
terraform init