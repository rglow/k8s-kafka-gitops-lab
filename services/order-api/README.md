# order-api

Spring Boot service that accepts HTTP orders and publishes them to Kafka.

## Local run

```bash
mvn spring-boot:run
```

## Build container image

```bash
mvn jib:dockerBuild
```

Default image name:

```text
order-api:0.1.0-SNAPSHOT
```

Import it into the current k3d cluster:

```bash
k3d image import order-api:0.1.0-SNAPSHOT -c platform-lab
```

Override the image name when needed:

```bash
mvn jib:dockerBuild -Dimage.name=ghcr.io/<owner>/order-api:dev
```
