resource "kubernetes_persistent_volume" "postgres_pv" {
  metadata {
    name = "postgres-pv"
  }
  spec {
    capacity = {
      storage = "1Gi"
    }
    access_modes = ["ReadWriteOnce"]
    persistent_volume_source {
      host_path {
        path = "/run/desktop/mnt/host/d/k8s-data/postgres" # Make sure this path exists on your Kubernetes nodes or adjust as needed
      }
    }
    storage_class_name = "standard" # Or your specific storage class
  }
}

resource "kubernetes_persistent_volume_claim" "postgres_pvc" {
  metadata {
    name = "postgres-pvc"
  }
  spec {
    access_modes = ["ReadWriteOnce"]
    resources {
      requests = {
        storage = "1Gi"
      }
    }
    storage_class_name = "standard" # Match the PV storage class
    volume_name        = kubernetes_persistent_volume.postgres_pv.metadata.0.name
  }
  depends_on = [
    kubernetes_persistent_volume.postgres_pv
  ]
}

resource "kubernetes_service" "postgres_service" {
  metadata {
    name = "postgres-service"
  }
  spec {
    selector = {
      app = "postgres"
    }
    port {
      protocol    = "TCP"
      port        = 5432
      target_port = 5432
    }
    type = "ClusterIP" # Default type if not specified, adjust if needed (e.g., NodePort, LoadBalancer)
  }
}