resource "kubernetes_deployment" "postgres_deployment" {
  metadata {
    name = "postgres-deployment"
  }
  spec {
    replicas = 1
    selector {
      match_labels = {
        app = "postgres"
      }
    }
    template {
      metadata {
        labels = {
          app = "postgres"
        }
      }
      spec {
        container {
          name  = "postgres"
          image = "postgres-service:latest" # Assuming this local image exists
          image_pull_policy = "Never"

          port {
            container_port = 5432
          }

          env {
            name  = "POSTGRES_USER"
            value = "coo_user" # Matches the secret
          }
          env {
            name  = "POSTGRES_PASSWORD"
            value = "coo123" # Matches the secret
          }
          env {
            name  = "POSTGRES_DB"
            value = "coo_db"
          }

          resources {
            requests = {
              memory = "64Mi"
              cpu    = "250m"
            }
            limits = {
              memory = "128Mi"
              cpu    = "500m"
            }
          }

          volume_mount {
            name       = "postgres-data"
            mount_path = "/var/lib/postgresql/data"
          }
        }

        volume {
          name = "postgres-data"
          persistent_volume_claim {
            claim_name = kubernetes_persistent_volume_claim.postgres_pvc.metadata.0.name
          }
        }
      }
    }
  }
  depends_on = [
    kubernetes_persistent_volume_claim.postgres_pvc,
    kubernetes_service.postgres_service
  ]
}

resource "kubernetes_deployment" "coo_service_deployment" {
  metadata {
    name = "coo-service"
  }
  spec {
    replicas = 1
    selector {
      match_labels = {
        app = "coo-service"
      }
    }
    template {
      metadata {
        labels = {
          app = "coo-service"
        }
      }
      spec {
        container {
          name  = "coo-service"
          image = "coo-service:latest"
          image_pull_policy = "Never"

          port {
            container_port = 8080
          }

          env {
            name  = "SPRING_DATASOURCE_URL"
            value = "jdbc:postgresql://postgres-service:5432/coo_db"
          }
          env {
            name = "SPRING_DATASOURCE_USERNAME"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.db_credentials.metadata.0.name
                key  = "SPRING_DATASOURCE_USERNAME"
              }
            }
          }
          env {
            name = "SPRING_DATASOURCE_PASSWORD"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.db_credentials.metadata.0.name
                key  = "SPRING_DATASOURCE_PASSWORD"
              }
            }
          }

          readiness_probe {
            http_get {
              path = "/actuator/health"
              port = 8080
            }
            initial_delay_seconds = 30
            period_seconds      = 10
          }
        }
      }
    }
  }
  depends_on = [
    kubernetes_deployment.postgres_deployment,
    kubernetes_secret.db_credentials
  ]
}

resource "kubernetes_deployment" "reply_service_deployment" {
  metadata {
    name = "reply-service"
  }
  spec {
    replicas = 1
    selector {
      match_labels = {
        app = "reply-service"
      }
    }
    template {
      metadata {
        labels = {
          app = "reply-service"
        }
      }
      spec {
        container {
          name  = "reply-service"
          image = "reply-service:latest"
          image_pull_policy = "Never"

          port {
            container_port = 8080
          }

          env {
            name  = "SPRING_DATASOURCE_URL"
            value = "jdbc:postgresql://postgres-service:5432/coo_db"
          }
          env {
            name = "SPRING_DATASOURCE_USERNAME"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.db_credentials.metadata.0.name
                key  = "SPRING_DATASOURCE_USERNAME"
              }
            }
          }
          env {
            name = "SPRING_DATASOURCE_PASSWORD"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.db_credentials.metadata.0.name
                key  = "SPRING_DATASOURCE_PASSWORD"
              }
            }
          }

          readiness_probe {
            http_get {
              path = "/actuator/health"
              port = 8080
            }
            initial_delay_seconds = 30
            period_seconds      = 10
          }
        }
      }
    }
  }
  depends_on = [
    kubernetes_deployment.postgres_deployment,
    kubernetes_secret.db_credentials
  ]
}

resource "kubernetes_deployment" "user_service_deployment" {
  metadata {
    name = "user-service"
  }
  spec {
    replicas = 1
    selector {
      match_labels = {
        app = "user-service"
      }
    }
    template {
      metadata {
        labels = {
          app = "user-service"
        }
      }
      spec {
        container {
          name  = "user-service"
          image = "user-service:latest"
          image_pull_policy = "Never"

          port {
            container_port = 8080
          }

          env {
            name  = "SPRING_DATASOURCE_URL"
            value = "jdbc:postgresql://postgres-service:5432/coo_db"
          }
          env {
            name = "SPRING_DATASOURCE_USERNAME"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.db_credentials.metadata.0.name
                key  = "SPRING_DATASOURCE_USERNAME"
              }
            }
          }
          env {
            name = "SPRING_DATASOURCE_PASSWORD"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.db_credentials.metadata.0.name
                key  = "SPRING_DATASOURCE_PASSWORD"
              }
            }
          }

          readiness_probe {
            http_get {
              path = "/actuator/health"
              port = 8080
            }
            initial_delay_seconds = 30
            period_seconds      = 10
          }
        }
      }
    }
  }
  depends_on = [
    kubernetes_deployment.postgres_deployment,
    kubernetes_secret.db_credentials
  ]
}

resource "kubernetes_deployment" "like_service_deployment" {
  metadata {
    name = "like-service"
  }
  spec {
    replicas = 1
    selector {
      match_labels = {
        app = "like-service"
      }
    }
    template {
      metadata {
        labels = {
          app = "like-service"
        }
      }
      spec {
        container {
          name  = "like-service"
          image = "like-service:latest"
          image_pull_policy = "Never"

          port {
            container_port = 8080
          }

          env {
            name  = "SPRING_DATASOURCE_URL"
            value = "jdbc:postgresql://postgres-service:5432/coo_db"
          }
          env {
            name = "SPRING_DATASOURCE_USERNAME"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.db_credentials.metadata.0.name
                key  = "SPRING_DATASOURCE_USERNAME"
              }
            }
          }
          env {
            name = "SPRING_DATASOURCE_PASSWORD"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.db_credentials.metadata.0.name
                key  = "SPRING_DATASOURCE_PASSWORD"
              }
            }
          }

          readiness_probe {
            http_get {
              path = "/actuator/health"
              port = 8080
            }
            initial_delay_seconds = 30
            period_seconds      = 10
          }
        }
      }
    }
  }
  depends_on = [
    kubernetes_persistent_volume_claim.postgres_pvc,
    kubernetes_service.postgres_service,
    kubernetes_secret.db_credentials
  ]
}