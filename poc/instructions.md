# PostgreSQL and Spring Boot Microservice in Kubernetes

This project demonstrates how to run a PostgreSQL database and Spring Boot microservices in Kubernetes using Docker Desktop.

## Project Structure

- `k8s/` - Kubernetes manifests for deploying PostgreSQL and the Spring Boot microservices
- `postgres/` - PostgreSQL initialization scripts and Dockerfile
- `user-service/` - Spring Boot microservice for user management
- `coo-service/` - Spring Boot microservice for coo management (similar to tweets)

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

### build postgres service docker image
- docker build -t postgres-service:latest .

### deploy postgres service & user service
- deploy.bat

### undeploy postgres service & user service
- undeploy.bat

### user service swagger url
- http://localhost/swagger-ui/index.html

### coo service swagger url
- http://localhost/swagger-ui/index.html

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
http://localhost/coo/api/coos