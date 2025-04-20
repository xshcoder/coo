resource "kubernetes_service" "coo_service" {
  metadata {
    name = "coo-service"
  }
  spec {
    selector = {
      app = "coo-service"
    }
    port {
      port        = 80
      target_port = 8080
    }
    type = "ClusterIP"
  }
  depends_on = [
    kubernetes_deployment.coo_service_deployment
  ]
}

resource "kubernetes_service" "reply_service" {
  metadata {
    name = "reply-service"
  }
  spec {
    selector = {
      app = "reply-service"
    }
    port {
      port        = 80
      target_port = 8080
    }
    type = "ClusterIP"
  }
  depends_on = [
    kubernetes_deployment.reply_service_deployment
  ]
}

resource "kubernetes_service" "user_service" {
  metadata {
    name = "user-service"
  }
  spec {
    selector = {
      app = "user-service"
    }
    port {
      port        = 80
      target_port = 8080
    }
    type = "ClusterIP"
  }
  depends_on = [
    kubernetes_deployment.user_service_deployment
  ]
}

resource "kubernetes_service" "like_service" {
  metadata {
    name = "like-service"
  }
  spec {
    selector = {
      app = "like-service"
    }
    port {
      port        = 80
      target_port = 8080
    }
    type = "ClusterIP"
  }
  depends_on = [
    kubernetes_deployment.like_service_deployment
  ]
}

resource "kubernetes_ingress_v1" "coo_ingress" {
  metadata {
    name = "coo-ingress"
    annotations = {
      "nginx.ingress.kubernetes.io/rewrite-target" = "/$2"
    }
  }
  spec {
    ingress_class_name = "nginx" # Make sure you have an Ingress controller like Nginx installed
    rule {
      http {
        path {
          path      = "/coo(/|$)(.*)"
          path_type = "ImplementationSpecific"
          backend {
            service {
              name = kubernetes_service.coo_service.metadata.0.name
              port {
                number = 80
              }
            }
          }
        }
        path {
          path      = "/like(/|$)(.*)"
          path_type = "ImplementationSpecific"
          backend {
            service {
              name = kubernetes_service.like_service.metadata.0.name
              port {
                number = 80
              }
            }
          }
        }
        path {
          path      = "/user(/|$)(.*)"
          path_type = "ImplementationSpecific"
          backend {
            service {
              name = kubernetes_service.user_service.metadata.0.name
              port {
                number = 80
              }
            }
          }
        }
        path {
          path      = "/like(/|$)(.*)"
          path_type = "ImplementationSpecific"
          backend {
            service {
              name = kubernetes_service.like_service.metadata.0.name
              port {
                number = 80
              }
            }
          }
        }
        path {
          path      = "/reply(/|$)(.*)"
          path_type = "ImplementationSpecific"
          backend {
            service {
              name = kubernetes_service.reply_service.metadata.0.name
              port {
                number = 80
              }
            }
          }
        }
        path {
          path      = "/like(/|$)(.*)"
          path_type = "ImplementationSpecific"
          backend {
            service {
              name = kubernetes_service.like_service.metadata.0.name
              port {
                number = 80
              }
            }
          }
        }
      }
    }
  }
  depends_on = [
    kubernetes_service.coo_service,
    kubernetes_service.reply_service,
    kubernetes_service.user_service
  ]
}