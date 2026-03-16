# Repository Guidelines

## Project Structure & Module Organization

This repository is a design-first Kubernetes lab for Kafka and GitOps workflows. Keep top-level intent clear:

- `docs/` holds project documentation: [`architecture.md`](./docs/architecture.md), [`runbook.md`](./docs/runbook.md), and the decision log in [`decisions.md`](./docs/decisions.md).
- `infra/` is reserved for platform assets such as `k3d/`, `gitops/`, and `helm/` manifests.
- `services/` contains workload directories such as `order-api/` and `order-processor/`.

Place new infrastructure manifests under the most specific `infra/` subdirectory available. Add architecture decisions as new `D<n>` entries instead of overwriting prior rationale.

## Build, Test, and Development Commands

The repo currently centers on local platform setup rather than an application build. Use:

- `docker ps` to verify Docker is running before cluster work.
- `k3d cluster create platform-lab --servers 1 --agents 1` to create the local lab cluster.
- `k3d cluster list` to inspect available clusters.
- `kubectl get nodes` to confirm cluster health after bootstrap.

If a service adds its own build or test tooling later, document the command in that service directory and mirror it in `README.md` or `docs/runbook.md`.

## Coding Style & Naming Conventions

Use Markdown for docs and YAML for Kubernetes manifests. Prefer short, explicit names aligned to the domain, such as `order-api` or `kafka-cluster`. Keep indentation consistent with file format defaults: 2 spaces for YAML, standard Markdown heading hierarchy for docs. Write in concise, imperative language and keep examples runnable.

## Testing Guidelines

There is no shared automated test suite yet. For infrastructure changes, validate with local cluster checks such as `kubectl get pods -A` and targeted resource inspection. For new services, add tests next to the service code and document how to run them. Name tests after the behavior they verify, not the implementation detail.

## Commit & Pull Request Guidelines

Recent history uses short, imperative commits like `updated runbook`, `Started the runbook`, and scoped setup messages like `init: project skeleton and structure`. Follow that pattern: one focused change per commit, subject line under about 72 characters, optional scope prefix when useful.

PRs should explain what changed, why it changed, and how it was validated locally. Link any relevant issue or decision log entry, and include command output or screenshots when the change affects cluster behavior or developer workflow.
