# 05 · SessionFactory and Session

## Introduction

These are the two objects you touch most. Getting their **roles and lifecycles** right is the single most important operational skill in Hibernate.

## Why this concept exists

Some work is expensive and should happen once (parsing mappings, opening a connection pool). Other work is cheap and per-request (running a few queries in a transaction). Hibernate separates the two: `SessionFactory` for the expensive, long-lived part; `Session` for the cheap, short-lived part.

## SessionFactory

- Built **once** per application (per database).
- **Thread-safe** — safely shared across the whole app.
- Heavyweight: holds mapping metadata, the JDBC connection pool, and the second-level cache.
- Its job is to hand out `Session` objects.

## Session

- Created **per unit of work** (typically per request/transaction).
- **NOT thread-safe** — one thread at a time, never shared.
- Lightweight and short-lived.
- Wraps a JDBC connection and the **first-level cache / persistence context**.
- Provides the core API: `persist`, `find`, `merge`, `remove`, `createQuery`.

## Important methods

| On | Method | Does |
|----|--------|------|
| `SessionFactory` | `openSession()` | Create a new session |
| `Session` | `beginTransaction()` | Start a transaction |
| `Session` | `persist / find / merge / remove` | CRUD |
| `Session` | `close()` | Release the session (use try-with-resources) |

## Simple example

```java
SessionFactory factory = HibernateUtil.getSessionFactory();   // shared, built once

try (Session session = factory.openSession()) {               // fresh per unit of work
    Transaction tx = session.beginTransaction();
    session.persist(new Student("Ada", "CS", "ada@example.com"));
    tx.commit();
}                                                             // auto-closed
```

▶️ Runnable: `com.example.session.SessionDemo`

## Explanation of the example

`HibernateUtil` caches the single factory. Each `openSession()` returns an independent session with its own first-level cache and JDBC connection. Try-with-resources guarantees `close()`.

## Best practices

- One `SessionFactory` for the app; build it via a util/singleton (or let Spring do it).
- Short sessions: open → work → commit → close.
- Always close sessions (try-with-resources).

## Common mistakes

- Sharing a `Session` between threads → corrupted state, random errors.
- Long-lived sessions accumulating entities in the first-level cache → memory bloat.
- Forgetting to close → leaked connections.

## Summary

`SessionFactory` = one heavy, thread-safe factory. `Session` = many light, non-thread-safe, short-lived units of work that carry the persistence context.

## How this appears in Spring Data JPA

- `SessionFactory` → `EntityManagerFactory`: **one** bean, auto-built by Spring Boot.
- `Session` → `EntityManager`: Spring injects a fresh, transaction-scoped one for each `@Transactional` call.

You stop calling `openSession()`/`close()` entirely — Spring manages the session lifecycle around your repository/service methods. But the "short session per unit of work" rule is exactly what `@Transactional` enforces.

---
➡️ **Next:** [06 · Entities](06-Entities.md)
