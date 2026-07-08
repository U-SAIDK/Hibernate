# 03 · Hibernate Architecture

## Introduction

Hibernate has a small number of core objects that fit together in a fixed pipeline. Once you know these five, the whole framework makes sense.

## Why this concept exists

You need one heavyweight object that reads your configuration and mappings **once**, and many lightweight objects that do the actual per-operation work. Splitting these responsibilities is what makes Hibernate both fast and safe to use.

## The core pipeline

```
   Configuration            reads hibernate.cfg.xml + entity metadata
        │
        ▼
   SessionFactory           built ONCE per app · thread-safe · expensive
        │  openSession()
        ▼
   Session                  one per unit-of-work · NOT thread-safe · cheap
        │  beginTransaction()
        ▼
   Transaction              commit() / rollback()
        │
        ▼
   JDBC ──► Database
```

## The five core components

| Component | Role | Lifecycle |
|-----------|------|-----------|
| **Configuration** | Reads settings + entity mappings, bootstraps everything | Startup only |
| **SessionFactory** | Factory for Sessions; holds mappings, connection pool, 2nd-level cache | One per application |
| **Session** | The main API: persist/find/merge/remove; holds the 1st-level cache | One per unit of work / request |
| **Transaction** | Wraps a database transaction (atomic commit/rollback) | One per unit of work |
| **Query** | HQL/JPQL/Criteria querying | As needed |

## Important detail: heavy vs light

- **`SessionFactory` is heavy** — building it parses all mappings and opens a connection pool. Build it *once* and share it. (In this course, `HibernateUtil` does exactly that.)
- **`Session` is light** — create one per unit of work and close it. It is **not thread-safe**; never share a `Session` between threads.

## Simple example

```java
// ONE SessionFactory for the whole app (built once in HibernateUtil):
SessionFactory factory = new Configuration().configure().buildSessionFactory();

// MANY short-lived sessions:
try (Session session = factory.openSession()) {
    Transaction tx = session.beginTransaction();
    // ... work ...
    tx.commit();
}
```

▶️ Runnable: `com.example.session.SessionDemo`

## Explanation of the example

`configure()` loads `hibernate.cfg.xml`; `buildSessionFactory()` produces the shared factory; each `openSession()` gives you an isolated conversation with the database.

## Best practices

- Exactly **one** `SessionFactory` per database, for the whole application lifetime.
- Open a `Session`, use it, close it (try-with-resources). Keep it short-lived.
- Never share a `Session` across threads.

## Common mistakes

- Rebuilding the `SessionFactory` per request — catastrophic for performance.
- Keeping one `Session` open for the entire app or sharing it between threads.

## Summary

`Configuration → SessionFactory (once) → Session (per unit of work) → Transaction → Database`. Heavy factory, light sessions.

## How this appears in Spring Data JPA

The mapping is direct:

| Hibernate | JPA / Spring |
|-----------|--------------|
| `SessionFactory` | `EntityManagerFactory` (one Spring bean) |
| `Session` | `EntityManager` (one per transaction) |
| `Transaction` | `@Transactional` |

Spring Boot builds the `EntityManagerFactory` bean at startup (your "SessionFactory") and injects a fresh `EntityManager` per transaction. You never call `openSession()` — but it is happening.

---
➡️ **Next:** [04 · Configuration](04-Configuration.md)
