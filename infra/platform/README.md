# Platform Manifests

Store GitOps-managed platform components here.

Examples:

- Flux-managed `HelmRelease` resources
- operators such as Strimzi
- shared namespaces, policies, and supporting infrastructure

This layer should be reconciled before application workloads.

The initial baseline in this repository is the `platform-system` namespace plus
the root `kustomization.yaml` for this layer.
