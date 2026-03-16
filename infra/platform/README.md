# Platform Manifests

Store GitOps-managed platform components here.

Examples:

- Flux-managed `HelmRelease` resources
- operators such as Strimzi
- shared namespaces, policies, and supporting infrastructure

This layer should be reconciled before application workloads.

The initial baseline in this repository is the `platform-system` namespace plus
the root `kustomization.yaml` for this layer.

Strimzi is installed here through Flux using a `HelmRepository` and
`HelmRelease`. The Kafka cluster custom resources should be added only after the
operator is healthy.

The local development Kafka cluster lives under `infra/platform/kafka/` and is
reconciled through a separate Flux `Kustomization` that depends on the platform
layer. In the current lab setup, the Kafka resources run in `platform-system`
because the Strimzi operator watches its own namespace.
