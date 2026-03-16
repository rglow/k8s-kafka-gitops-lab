# Decision Log

## D1: Local Kubernetes Distribution

### Context

The project requires a lightweight local Kubernetes cluster that:

- runs reliably on a developer machine
- has low resource footprint
- supports Helm deployments
- allows experimentation with GitOps tools
- does not introduce unnecessary operational complexity

The cluster is intended for development and architectural experimentation,
not for production simulation.

### Options Considered

#### 1) k3s
Lightweight Kubernetes distribution (can run directly on Linux).

**Pros:**
- Very lightweight
- Production-capable in edge scenarios
- No Docker dependency required

**Cons:**
- Not fully upstream Kubernetes
- Requires some setup and management effort
- Slightly less convenient for rapid teardown/recreate cycles than docker based solutions

#### 2) k3d
K3s running inside Docker containers.

**Pros:**
- Very fast startup
- Low memory footprint
- Simple cluster lifecycle (create/delete)
- Built-in load balancer
- Well suited for local labs

**Cons:**
- Based on k3s (not full upstream Kubernetes)
- Slight behavioral differences vs managed clusters

#### 3) kind (Kubernetes IN Docker)
Upstream Kubernetes running inside Docker containers.

**Pros:**
- Very close to upstream Kubernetes
- Widely used for CI and testing
- Good ecosystem support

**Cons:**
- Networking and ingress setup can require additional configuration
- Slightly heavier than k3d in some scenarios

#### 4) minikube
Single-node local Kubernetes environment.

**Pros:**
- Mature project
- Built-in addons
- Flexible drivers

**Cons:**
- Can be heavier on resources
- More opinionated
- Slower lifecycle compared to k3d

### Decision

**Selected: k3d**

### Rationale

k3d offers the best balance between:

- low resource usage
- fast development loop
- simplicity
- ease of experimentation

For the purpose of this lab, full upstream fidelity is less important than
developer experience and iteration speed.

### Future Consideration

If needed, the same setup can be validated against:

- kind (to increase upstream alignment)
- a managed Kubernetes cluster (EKS / AKS) for production parity

## D2: GitOps Tooling

### Context

The project needs a GitOps controller to manage platform components and,
later, Kafka-related infrastructure from Git.

The selected tool should:

- work well for infrastructure-first workflows
- support Helm-based operators and plain manifests
- fit a local `k3d` development cluster
- keep the operational model simple

The near-term goal is to install a Kafka operator and manage it declaratively.

### Options Considered

#### 1) Flux

**Pros:**
- Kubernetes-native controller model
- Strong fit for infrastructure reconciliation
- Good support for `Kustomization`, `HelmRelease`, and dependency ordering
- Lightweight operational footprint

**Cons:**
- Less UI support out of the box
- Slightly steeper learning curve when debugging through CRDs only

#### 2) Argo CD

**Pros:**
- Mature and user-friendly UI
- Good visibility into application sync state
- Strong ecosystem and broad adoption

**Cons:**
- Better aligned to application delivery workflows than infra-first use cases
- Adds more platform surface area than needed for this lab

### Decision

**Selected: Flux**

### Rationale

Flux is a better fit for this lab because the repository is currently focused on:

- infrastructure components
- operator-based installations
- Git as the primary control plane
- simple local experimentation

For the next phase, Flux provides a clean path to install and manage
Kafka-related components such as Strimzi using declarative resources.

### Future Consideration

Argo CD remains a valid alternative if the project later expands toward:

- richer application delivery workflows
- multi-team self-service use cases
- stronger UI-driven operations
