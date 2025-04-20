terraform {
  required_providers {
    kubernetes = {
      source  = "hashicorp/kubernetes"
      version = "~> 2.0"
    }
  }
}

provider "kubernetes" {
  # Configuration options
  # Assuming kubeconfig is configured correctly in the environment
  # or specify config_path = "~/.kube/config"
}