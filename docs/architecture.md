# Architecture Overview

## Purpose

This repository is a local platform lab for learning how to run Kafka on
Kubernetes with a GitOps operating model. The focus is on infrastructure,
delivery workflows, and event-driven service integration rather than
production-grade scale.

## Local Environment Assumptions

- Kubernetes runs locally on `k3d`
- the environment is `dev` only
- the cluster is non-HA and optimized for low resource usage
- components should be easy to tear down and recreate
- upstream parity matters less than fast iteration

This means initial platform components should prefer simple defaults over
high-availability settings.

## Repository Layout

The repository is intended to evolve toward this structure:

- `docs/` for architecture notes, runbooks, and decision records
- `infra/k3d/` for local cluster bootstrap assets
- `infra/gitops/` for Flux bootstrap and reconciliation configuration
- `infra/helm/` for shared Helm-related values or chart wrappers when needed
- `infra/platform/` for platform services managed by GitOps
- `infra/apps/` for application deployment manifests managed by GitOps
- `services/` for sample workloads such as producers and consumers

## Target Delivery Model

The delivery model is infrastructure-first:

1. bootstrap the local cluster
2. install Flux
3. reconcile platform services from Git
4. install Kafka operator and cluster resources
5. deploy sample services that produce to and consume from Kafka

This ordering keeps the project consistent with GitOps from the beginning and
avoids mixing manual cluster changes with declarative platform management.

## Bootstrap Convention

Flux should be bootstrapped with `infra/gitops/` as the repository path.
Generated Flux sync artifacts stay there, while reconciled resources live under:

- `infra/platform/` for shared infrastructure and operators
- `infra/apps/` for workloads running on the platform

The first reconciliation target beyond Flux itself is the `platform`
`Kustomization`, which points at `infra/platform/`.

The current platform bootstrap sequence is:

1. create `platform-system`
2. install the Strimzi operator through Flux
3. add Kafka custom resources after the operator and CRDs are ready

Kafka cluster resources are reconciled separately from the operator install so
that Flux can wait for the Strimzi CRDs before applying `Kafka` and
`KafkaNodePool` resources.
