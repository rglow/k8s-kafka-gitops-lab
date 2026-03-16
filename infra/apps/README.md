# Application Manifests

Store GitOps-managed application workloads here.

Examples:

- `Deployment`, `Service`, and `ConfigMap` resources for sample services
- application-specific `HelmRelease` resources
- Kafka-connected workloads such as `order-api` and `order-processor`

Application resources should depend on the platform layer being ready first.
