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
