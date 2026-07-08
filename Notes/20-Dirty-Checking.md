# 20 · Dirty Checking

## Introduction

**Dirty checking** is Hibernate's ability to automatically detect changes to managed entities and issue the matching `UPDATE` — **without you calling any update method**.

## Why this concept exists

Manually tracking which fields changed and writing `UPDATE` statements is tedious and error-prone. Since Hibernate already holds a snapshot of each managed entity, it can detect changes for you.

## Internal working

1. When an entity becomes **persistent** (loaded or persisted), Hibernate stores a **snapshot** of its state in the persistence context.
2. During **flush** (usually at commit), Hibernate compares each managed entity to its snapshot.
3. Any entity whose fields differ is "dirty" → Hibernate generates an `UPDATE` for the changed columns.

```
snapshot: course = "CS"      current: course = "Distributed Systems"
                        ↓ differ
             UPDATE students SET course = ? WHERE id = ?
```

## Simple example

```java
Student s = session.find(Student.class, id);   // persistent + snapshot taken
s.setCourse("Fault-Tolerant Systems");         // just a setter
tx.commit();                                    // dirty checking → UPDATE
```

▶️ Runnable: `com.example.dirtychecking.DirtyCheckingDemo`

## Explanation of the example

There is no `update()` call anywhere. Because `s` is managed, changing its field marks it dirty, and the commit-time flush writes the `UPDATE`. This only works while the entity is **persistent** (Note 07) — a detached entity's changes are ignored.

## When does flush happen?

Hibernate flushes (and thus dirty-checks) automatically:

- before a transaction **commit**,
- before running a query that could be affected by pending changes,
- when you call `flush()` explicitly.

## Best practices

- Prefer dirty checking over manual `merge()` for entities that are already managed — it's simpler and updates only changed columns.
- Do your mutations inside the transaction so the entity is persistent.
- For read-only work, a read-only transaction skips snapshotting overhead.

## Common mistakes

- Changing a **detached** entity and expecting an update (it won't happen — reattach with `merge` first).
- Calling `merge()`/`update()` unnecessarily on an already-managed entity.
- Being surprised by an `UPDATE` you "didn't ask for" — it came from a field you changed on a managed entity.

## Summary

Dirty checking compares each managed entity to its load-time snapshot at flush and auto-generates `UPDATE`s for changed fields. It works only for persistent entities and needs no explicit update call.

## How this appears in Spring Data JPA

This is why Spring service code like:

```java
@Transactional
public void rename(Long id, String course) {
    Student s = repo.findById(id).orElseThrow();
    s.setCourse(course);        // no repo.save() needed!
}
```

…persists the change **without calling `save()`**. Beginners often add a redundant `save()`; it's harmless but unnecessary because dirty checking already fires at the `@Transactional` boundary. Understanding this removes a lot of "do I need to call save?" confusion in Spring.

---
➡️ **Next:** [21 · First-Level Cache](21-First-Level-Cache.md)
