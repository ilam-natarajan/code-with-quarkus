# code-with-quarkus

A Quarkus REST API demo project that showcases Spring Framework integration within Quarkus — specifically using `RequestContextHolder` for request-scoped data and HTTP header propagation via JAX-RS filters.

Built with [Quarkus 3.19.3](https://quarkus.io/) and Java 21.

## Overview

This project demonstrates:

- **REST endpoints** with JAX-RS (`/hello`, `/hello/header`)
- **Spring Web integration** — using `RequestContextHolder` in a Quarkus app
- **Custom `RequestAttributes`** implementation backed by `ConcurrentHashMap` for request-scoped storage
- **JAX-RS filter** that extracts the `x-test-header` HTTP header and stores it in request context
- **CDI dependency injection** with application-scoped and request-scoped beans
- **Startup lifecycle** with `@PostConstruct` initialization

## Project Structure

```
src/
├── main/java/org/acme/
│   ├── GreetingResource.java               # REST controller — GET /hello, GET /hello/header
│   ├── PaymentService.java                 # Application-scoped service with startup init
│   ├── QuarkusRequestAttributes.java       # Custom Spring RequestAttributes implementation
│   └── TestHeaderRequestContextFilter.java # JAX-RS filter for x-test-header extraction
├── main/resources/
│   └── application.properties
└── test/java/org/acme/
    ├── GreetingResourceTest.java           # Unit tests (QuarkusTest)
    └── GreetingResourceIT.java             # Native image integration tests
```

## API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/hello` | Returns a greeting string via `PaymentService` |
| `GET` | `/hello/header` | Returns the value of the `x-test-header` request header (or `"missing"` if absent) |

**Example:**
```bash
curl http://localhost:8080/hello
curl -H "x-test-header: abc123" http://localhost:8080/hello/header
```

## Prerequisites

- Java 21+
- Gradle (or use the included `./gradlew` wrapper)
- _(Optional)_ GraalVM for native builds

## Running

### Dev mode (hot reload)

```bash
./gradlew quarkusDev
```

The Dev UI is available at <http://localhost:8080/q/dev/> in dev mode only.

### Run packaged JAR

```bash
./gradlew build
java -jar build/quarkus-app/quarkus-run.jar
```

### Über-JAR

```bash
./gradlew build -Dquarkus.package.jar.type=uber-jar
java -jar build/*-runner.jar
```

## Testing

```bash
# Unit tests
./gradlew test

# All verification (tests + checks)
./gradlew check
```

## Native Executable

Build with GraalVM installed:

```bash
./gradlew build -Dquarkus.native.enabled=true
./build/code-with-quarkus-1.0.0-SNAPSHOT-runner
```

Build using Docker (no local GraalVM required):

```bash
./gradlew build -Dquarkus.native.enabled=true -Dquarkus.native.container-build=true
```

Run native integration tests:

```bash
./gradlew testNative
```

## Key Dependencies

| Dependency | Purpose |
|-----------|---------|
| `quarkus-arc` | CDI dependency injection |
| `quarkus-resteasy` | JAX-RS REST support |
| `quarkus-undertow` | Servlet container |
| `spring-web` / `spring-context` 6.2.8 | Spring `RequestContextHolder` integration |
| `quarkus-junit5` + `rest-assured` | Testing |

## Learn More

- [Quarkus Documentation](https://quarkus.io/guides/)
- [Quarkus Gradle Tooling](https://quarkus.io/guides/gradle-tooling)
- [Spring DI compatibility in Quarkus](https://quarkus.io/guides/spring-di)
