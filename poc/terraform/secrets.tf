resource "kubernetes_secret" "db_credentials" {
  metadata {
    name = "db-credentials"
  }

  data = {
    # IMPORTANT: Replace these placeholder values with your actual credentials.
    # Consider using Terraform variables, environment variables, or a secrets management tool for production.
    SPRING_DATASOURCE_USERNAME = "coo_user" # Placeholder - Corresponds to POSTGRES_USER in postgres-deployment.yaml
    SPRING_DATASOURCE_PASSWORD = "coo123"   # Placeholder - Corresponds to POSTGRES_PASSWORD in postgres-deployment.yaml
  }

  type = "Opaque"
}