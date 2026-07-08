# 09 · CRUD Operations

## Introduction

CRUD = **C**reate, **R**ead, **U**pdate, **D**elete. These are the four basic persistence operations, and in modern Hibernate they map to four JPA methods.

## Why this concept exists

Almost everything an application does with data is a CRUD operation. Hibernate gives you object-oriented methods for each, generating the SQL for you.

## The modern (JPA) API

| Operation | Method | Notes |
|-----------|--------|-------|
| Create | `session.persist(entity)` | Insert a new (transient) entity |
| Read | `session.find(Class, id)` | Returns the entity or `null` |
| Update | *mutate a managed entity* | **Dirty checking** — no method call |
| Delete | `session.remove(entity)` | Entity must be managed |
| Update detached | `session.merge(entity)` | Reattach + copy changes |

> Older tutorials use `save()`, `update()`, `saveOrUpdate()`, `delete()`, `get()`. Prefer the JPA names above — they are what Spring Data JPA uses.

## Simple example

```java
// CREATE
Transaction tx = session.beginTransaction();
Student s = new Student("Grace Hopper", "Compilers", "grace@ex.com");
session.persist(s);
tx.commit();

// READ
Student loaded = session.find(Student.class, s.getId());

// UPDATE — no update() call
tx = session.beginTransaction();
Student managed = session.find(Student.class, s.getId());
managed.setCourse("Distributed Systems");   // change is auto-detected
tx.commit();                                 // UPDATE flushed here

// DELETE
tx = session.beginTransaction();
session.remove(session.find(Student.class, s.getId()));
tx.commit();
```

▶️ Runnable: `com.example.crud.CrudDemo`

## Explanation of the example

The UPDATE is the interesting part: we never call an update method. Because the loaded `Student` is *persistent*, changing its `course` field is picked up by dirty checking and flushed as an `UPDATE` at commit.

## `find()` vs `getReference()`

- `find()` → runs the SELECT immediately, returns the entity or `null`.
- `getReference()` → returns a lazy **proxy**, no SELECT until you access a field; throws if the row doesn't exist. Useful when you only need the id (e.g. to set a foreign key).

## Best practices

- Wrap every write in a transaction.
- Use `persist` for new entities, `merge` for detached ones.
- Prefer updating via dirty checking over manual `merge` when the entity is already managed.
- Check for `null` from `find()`.

## Common mistakes

- Calling `merge()` on an already-managed entity (unnecessary; just mutate it).
- Forgetting the transaction → nothing is committed.
- Expecting `persist()` on an entity with an existing id to "update" it — use `merge`.
- Using deprecated `save()/update()` habits from old tutorials.

## Summary

`persist` (create), `find` (read), dirty checking or `merge` (update), `remove` (delete) — all inside a transaction. Update usually needs no method call at all.

## How this appears in Spring Data JPA

`JpaRepository` wraps these exact methods:

| Spring repository call | Underlying JPA/Hibernate |
|------------------------|--------------------------|
| `save(entity)` | `persist` if new, else `merge` |
| `findById(id)` | `find` (returns `Optional`) |
| *change a field in a `@Transactional` method* | dirty checking |
| `delete(entity)` / `deleteById(id)` | `remove` |
| `getReferenceById(id)` | `getReference` |

So `save()` is not magic — it's `persist`/`merge` chosen for you based on whether the entity is new.

---
➡️ **Next:** [10 · Transactions](10-Transactions.md)
