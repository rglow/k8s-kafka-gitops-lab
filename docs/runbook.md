# Runbook

---

## 1. Create Local Kubernetes Cluster (k3d)

### Prerequisites

- Docker
- kubectl
- k3d
- helm

Verify Docker is running:

```
docker ps
```

---

### Create cluster

```
k3d cluster create platform-lab --servers 1 --agents 1
```

There are other commands that can be used instead of `create`: 
* `delete`, `stop` and `start` followed by a cluster name allow for cluster management
* `list` lists all the created clusters

Verify the cluster is available:

```bash
kubectl config current-context
kubectl get nodes -o wide
```

---

## 2. Install Flux CLI

### Prerequisites

- running `k3d` cluster
- `kubectl` configured against the local cluster
- `flux` CLI installed locally

Verify the CLI is available:

```bash
flux --version
```

Install Flux controllers into the cluster:

```bash
flux install
```

### Verify installation

```bash
kubectl get namespace flux-system
kubectl get pods -n flux-system
```

Expected result: `source-controller`, `kustomize-controller`,
`helm-controller`, and `notification-controller` should become `Running`.

---

## 3. Bootstrap Flux Against Git

Use CLI bootstrap once the repository structure is ready and the Git remote is
available.

Example for GitHub:

```bash
flux bootstrap github \
  --owner <github-user-or-org> \
  --repository k8s-kafka-gitops-lab \
  --branch main \
  --path ./infra/gitops
```

Example for a generic Git server:

```bash
flux bootstrap git \
  --url ssh://git@<git-host>/<owner>/k8s-kafka-gitops-lab.git \
  --branch main \
  --path ./infra/gitops
```

Before running bootstrap, ensure these directories exist in the repository:

- `infra/gitops/` as the Flux root
- `infra/platform/` for infrastructure manifests
- `infra/apps/` for workload manifests

### Verify bootstrap

```bash
flux get sources git
flux get kustomizations -A
kubectl get gitrepositories,kustomizations -A
```

Expected result: Flux should reconcile the Git source and create the
`Kustomization` objects defined under `infra/gitops/`.

### Notes

- prefer CLI bootstrap over Terraform for this local lab
- keep `infra/gitops/` as the reconciliation root
- manage platform components first, then Kafka operator, then workloads
