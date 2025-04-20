kubectl apply -f k8s/postgres-persistent-volume.yaml
kubectl apply -f k8s/postgres-pvc.yaml
kubectl apply -f k8s/postgres-service.yaml
kubectl apply -f k8s/postgres-deployment.yaml
kubectl create secret generic db-credentials --from-literal=SPRING_DATASOURCE_USERNAME=coo_user --from-literal=SPRING_DATASOURCE_PASSWORD=coo123
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.12.1/deploy/static/provider/cloud/deploy.yaml
kubectl apply -f k8s/ingress.yaml
kubectl apply -f k8s/user-service.yaml
kubectl apply -f k8s/user-service-deployment.yaml
kubectl apply -f k8s/coo-service.yaml
kubectl apply -f k8s/coo-service-deployment.yaml
kubectl apply -f k8s/reply-service.yaml
kubectl apply -f k8s/reply-service-deployment.yaml
kubectl apply -f k8s/like-service.yaml
kubectl apply -f k8s/like-service-deployment.yaml