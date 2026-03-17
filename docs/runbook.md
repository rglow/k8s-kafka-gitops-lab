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

### First reconciliation target

After bootstrap, add a repository-level `kustomization.yaml` under
`infra/gitops/` and define the first Flux-managed target for `infra/platform/`.

Minimal example:

```yaml
apiVersion: kustomize.config.k8s.io/v1beta1
kind: Kustomization
resources:
  - flux-system
  - platform-kustomization.yaml
```

The corresponding Flux `Kustomization` should point to `./infra/platform`.

---

## 4. Install Strimzi Through Flux

Strimzi should be installed as part of the `infra/platform/` layer, not by
running `helm install` manually.

Recommended resources:

- `HelmRepository` pointing to `https://strimzi.io/charts/`
- `HelmRelease` for the `strimzi-kafka-operator` chart

Suggested order:

1. commit the Strimzi manifests under `infra/platform/strimzi/`
2. push to `main`
3. wait for Flux to reconcile the `platform` `Kustomization`
4. verify the operator before creating any `Kafka` custom resources

Verification commands:

```bash
flux get helmrepositories -A
flux get helmreleases -A
kubectl get pods -n platform-system
kubectl get crds | grep kafka.strimzi.io
```

---

## 5. Deploy a Minimal Kafka Cluster

Deploy the Kafka cluster only after the Strimzi operator is healthy and the CRDs
are present.

Recommended local dev shape:

- one `KafkaNodePool`
- one Kafka node with dual broker and controller roles
- ephemeral storage
- small resource requests and limits

In the current setup, the Kafka resources are applied to `platform-system`
because the Strimzi operator watches only its own namespace.

Keep the Kafka cluster in a separate Flux `Kustomization` with `dependsOn` set
to the platform layer. This avoids applying `Kafka` resources before the Strimzi
CRDs exist.

Verification commands:

```bash
flux get kustomizations -A
kubectl get kafkas,kafkanodepools -n platform-system
kubectl get pods -n platform-system
kubectl wait kafka/kafka-dev -n platform-system --for=condition=Ready --timeout=10m
```

---

## 6. Reconcile Kafka Topics and Users for Demo Apps

The application layer can manage Kafka resources needed by sample workloads.
The current `order-demo` slice creates:

- `orders.incoming`
- `orders.processed`
- `orders.dlq`
- Kafka users `order-api` and `order-processor`

Flux reconciles this layer from `infra/apps/` after the Kafka cluster is ready.

Verification commands:

```bash
flux get kustomizations -A
kubectl get kafkatopics,kafkausers -n platform-system
kubectl get secret order-api order-processor -n platform-system
```

Check topic details:

```bash
kubectl get kafkatopic orders-incoming -n platform-system -o yaml
kubectl get kafkatopic orders-processed -n platform-system -o yaml
kubectl get kafkatopic orders-dlq -n platform-system -o yaml
```
