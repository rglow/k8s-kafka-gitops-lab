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
