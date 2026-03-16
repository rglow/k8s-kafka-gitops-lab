# Flux Bootstrap Root

This directory is the Flux bootstrap root used with:

```bash
flux bootstrap <provider> --path ./infra/gitops
```

After bootstrap, Flux-generated files such as `gotk-components.yaml` and
`gotk-sync.yaml` are expected to appear here.

Recommended reconciliation split for this repository:

- `../platform/` for infrastructure components such as operators and shared services
- `../apps/` for sample workloads deployed on top of the platform

Keep this directory focused on Flux bootstrap artifacts and top-level sync
configuration.
