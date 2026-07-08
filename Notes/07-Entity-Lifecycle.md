# 07 · Entity Lifecycle

> *An entity is not just a Java object. While Hibernate manages it, it moves through distinct states.*

## Introduction

Every entity instance is, at any moment, in one of **four states**. Whether a change you make hits the database depends entirely on which state the object is in.

## Why this concept exists

Hibernate can only auto-save changes to objects it is *managing*. The lifecycle defines when Hibernate is watching an object (and will persist your changes) and when it is not (and will silently ignore them). Confusion here is the #1 source of "why didn't my update save?" bugs.

## The four states

```
              new Student()
                    │
                    ▼
             🟡 Transient           (just a Java object; Hibernate knows nothing)
                    │  persist()
                    ▼
        🟢 Persistent / Managed     (tracked; changes auto-saved via dirty checking)
          │                    │
 detach() │                    │ remove()
 clear()  │                    │
 close()  ▼                    ▼
      🔵 Detached          🔴 Removed
          │                    │  commit()/flush()
          │ merge()            ▼
          ▼                🗑 row deleted
   🟢 Persistent again
```

| State | In DB? | Managed by Hibernate? | Changes auto-saved? |
|-------|:------:|:---------------------:|:-------------------:|
| 🟡 Transient | ❌ | ❌ | ❌ |
| 🟢 Persistent | ✅ | ✅ | ✅ (dirty checking) |
| 🔵 Detached | ✅ | ❌ | ❌ |
| 🔴 Removed | pending delete | ✅ | — |

## Internal working

A **Persistent** entity lives inside the **persistence context** (Note 08). Hibernate keeps a *snapshot* of it. On flush/commit it compares the entity to the snapshot and writes SQL for any difference (**dirty checking**, Note 20). Transient and detached entities are *not* in the context, so no snapshot, no auto-save.

## Important methods (state transitions)

| Method | From → To |
|--------|-----------|
| `new` | — → Transient |
| `persist()` | Transient → Persistent |
| `find()` / query | (DB) → Persistent |
| `detach()` / `clear()` / `close()` | Persistent → Detached |
| `merge()` | Detached → Persistent |
| `remove()` | Persistent → Removed |
| `commit()` / `flush()` | Removed → deleted row |

## Simple example

```java
Student s = new Student("Linus", "OS", "linus@ex.com"); // 🟡 Transient
session.persist(s);                                      // 🟢 Persistent
s.setCourse("Kernel Dev");                               // tracked; UPDATE on commit
session.detach(s);                                       // 🔵 Detached
s.setCourse("IGNORED");                                  // NOT saved
Student managed = session.merge(s);                      // 🟢 Persistent again
session.remove(managed);                                 // 🔴 Removed
tx.commit();                                             // 🗑 DELETE executed
```

▶️ Runnable: `com.example.lifecycle.EntityLifecycleDemo`

## Explanation of the example

The `setCourse("Kernel Dev")` on a *persistent* object is saved automatically. The identical `setCourse("IGNORED")` on a *detached* object does nothing — Hibernate isn't watching it anymore. `merge()` brings it back under management.

## Best practices

- Do your mutations while the entity is **persistent** (inside an open session/transaction).
- Use `merge()` to reattach objects that crossed a session boundary (e.g. came from a web form).
- Understand that `merge()` returns a *new* managed instance — keep using the returned object.

## Common mistakes

- Changing a **detached** entity and expecting it to save.
- Assuming `merge()` updates the object you passed in (it returns the managed copy instead).
- Modifying entities after the transaction/session closed.

## Summary

Four states — **Transient, Persistent, Detached, Removed**. Only **Persistent** entities have their changes auto-saved. Transitions are driven by `persist`, `find`, `detach`, `merge`, `remove`, and `commit`.

## How this appears in Spring Data JPA

Same four JPA states, same rules — Spring just moves the boundaries:

- `repository.save(newEntity)` → `persist` (Transient → Persistent).
- `repository.save(detachedEntity)` → `merge` (Detached → Persistent) and returns the managed instance — that's why you should keep the **returned** object.
- Inside a `@Transactional` method, loaded entities are **Persistent**, so changing a field saves it with no explicit `save()` call.
- When the transaction ends, entities become **Detached** — the classic `LazyInitializationException` happens when you touch a lazy field on such an entity.

---
➡️ **Next:** [08 · Persistence Context](08-Persistence-Context.md)
