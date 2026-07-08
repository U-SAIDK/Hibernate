# 08 · Persistence Context

## Introduction

The **persistence context** is the in-memory area, owned by a `Session`, that holds all currently *managed* (persistent) entities. It is also known as the **first-level cache**. It is the brain behind dirty checking, identity guarantees, and query de-duplication.

## Why this concept exists

Hibernate needs a place to remember which objects it is managing, what they looked like when loaded, and which id maps to which object. That memory *is* the persistence context. Without it, dirty checking and the identity guarantee would be impossible.

## Internal working

For every managed entity the context keeps:

1. The **entity instance** itself, keyed by its identifier.
2. A **snapshot** of its state at load time (used by dirty checking).

From this it provides three guarantees:

- **Identity guarantee** — within one session, the same id always returns the *same object instance* (`a == b`).
- **Automatic dirty checking** — at flush time, entities are compared to their snapshots and changed ones are updated.
- **Repeatable reads / cache** — loading the same id twice hits the DB once; the second read is served from memory.

## Key idea: it is scoped to the Session

```
Session
 └── Persistence Context (first-level cache)
       ├── Student#1  (+ snapshot)
       ├── Student#2  (+ snapshot)
       └── ...
```

Close the session → the context is gone → those entities become **detached**.

## Simple example

```java
try (Session session = factory.openSession()) {
    Student a = session.find(Student.class, 1L);  // SELECT runs
    Student b = session.find(Student.class, 1L);  // NO SELECT — served from context
    System.out.println(a == b);                   // true: same instance
}
```

▶️ Runnable: `com.example.caching.FirstLevelCacheDemo`

## Explanation of the example

The second `find` doesn't touch the database — the entity is already in the persistence context, and Hibernate returns the very same object (`a == b`).

## Best practices

- Keep the context small and short-lived: one per unit of work.
- Call `flush()`/`clear()` when batch-processing thousands of rows to avoid the context growing unbounded.
- Rely on the identity guarantee instead of comparing entities field-by-field within a session.

## Common mistakes

- Expecting the first-level cache to be shared across sessions — it is **not** (each session has its own).
- Long-running sessions that accumulate huge numbers of managed entities → memory problems.
- Being surprised that a second `find` produced no SQL (that's the cache working, not a bug).

## Summary

The persistence context (first-level cache) is a per-session map of managed entities + snapshots. It powers dirty checking, guarantees object identity, and avoids duplicate SELECTs.

## How this appears in Spring Data JPA

Every `@Transactional` method runs with exactly one persistence context (one `EntityManager`) behind the scenes. That's why:

- Entities you load in a service method are managed, so field changes save automatically.
- The same id returns the same instance within that method.
- After the method (transaction) ends, the context closes and entities detach — touching a lazy association then throws `LazyInitializationException`.

Understanding the persistence context is the key to understanding almost every "surprising" Spring Data JPA behavior.

---
➡️ **Next:** [09 · CRUD Operations](09-CRUD-Operations.md)
