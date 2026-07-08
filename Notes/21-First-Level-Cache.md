# 21 · First-Level Cache

## Introduction

The **first-level cache** is the per-`Session` cache of managed entities — it *is* the persistence context (Note 08), viewed from a performance angle. It is always on and cannot be disabled.

## Why this concept exists

Within one unit of work you often touch the same entity more than once. Without a cache, each access would hit the database. The first-level cache guarantees one SELECT per id per session, and guarantees you always get the *same object instance* for a given id.

## Internal working

The `Session` keeps a map: `identifier → entity instance`. On `find(id)`:

1. If the id is already in the map → return the cached instance, **no SQL**.
2. Otherwise → run the SELECT, store the result in the map, return it.

## Simple example

```java
try (Session session = factory.openSession()) {
    Student a = session.find(Student.class, id);  // SELECT runs
    Student b = session.find(Student.class, id);  // no SELECT — from cache
    System.out.println(a == b);                   // true (same instance)
}
```

▶️ Runnable: `com.example.caching.FirstLevelCacheDemo`

## Explanation of the example

The second `find` produces no SQL — Hibernate returns the cached object. And `a == b` is `true`: the cache enforces object identity within the session.

## Scope: per session

- The first-level cache is **bound to the Session**. Two different sessions have two different caches.
- Closing the session discards its cache; the entities become detached.
- It is **not** shared across the application (that would be the optional *second-level cache*, out of scope for this course).

## Best practices

- Rely on it — reading the same entity twice in a method is cheap.
- For large batch jobs, periodically `flush()` then `clear()` the session so the cache doesn't grow unbounded.
- Don't confuse it with the second-level cache (cross-session, optional, needs setup).

## Common mistakes

- Expecting the first-level cache to help *across* requests/sessions — it won't.
- Loading hundreds of thousands of entities in one session and running out of memory (no `clear()`).
- Thinking a "missing" SELECT in the log is a bug — it's the cache doing its job.

## Summary

The first-level cache is the always-on, per-session cache of managed entities. It avoids duplicate SELECTs within a session and guarantees one object instance per id. It disappears when the session closes.

## How this appears in Spring Data JPA

Each `@Transactional` method has exactly one session → one first-level cache. So repeated `findById(sameId)` within one transactional method hits the DB once. This also explains a subtlety: a JPQL bulk `UPDATE`/`DELETE` bypasses the persistence context, so entities already cached in the session can become **stale** — you may need `clear()`/`flush()`. These are the same mechanics you learned here, just under Spring's transaction boundary.

---
➡️ **Next:** [22 · Optimistic Locking](22-Optimistic-Locking.md)
