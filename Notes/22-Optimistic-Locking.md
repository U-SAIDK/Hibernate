# 22 · Optimistic Locking

## Introduction

**Optimistic locking** prevents *lost updates* when two transactions edit the same row concurrently — without holding database locks. You add a `@Version` field and Hibernate does the rest.

## Why this concept exists

Consider two users who both load account balance = 100. User A adds 50 and saves (150). User B, still holding the old 100, subtracts 30 and saves (70) — **A's change is lost**. Optimistic locking detects this conflict and rejects the stale write.

## Internal working

With a `@Version` column, every `UPDATE` becomes:

```sql
UPDATE accounts SET balance = ?, version = version + 1
WHERE id = ? AND version = ?      -- the version we loaded
```

If another transaction already bumped the version, `WHERE ... AND version = oldValue` matches **zero rows**. Hibernate sees 0 rows updated and throws an **`OptimisticLockException`** (`StaleObjectStateException`).

"Optimistic" = assume conflicts are rare, check only at write time (no locks held meanwhile). Contrast with **pessimistic** locking (`SELECT ... FOR UPDATE`), which locks the row up front.

## Important annotations

```java
@Version
private int version;   // Hibernate manages this — never set it yourself
```

A `@Version` field can be `int`/`long` (a counter) or a `Timestamp`.

## Simple example

```java
// Both sessions load the row at version 0
Account a = sessionA.find(Account.class, id);
Account b = sessionB.find(Account.class, id);

a.setBalance(a.getBalance() + 50);
txA.commit();                 // version 0 → 1, succeeds

b.setBalance(b.getBalance() - 30);
txB.commit();                 // expects version 0, none match → OptimisticLockException
```

▶️ Runnable: `com.example.locking.OptimisticLockingDemo`

## Explanation of the example

Session A commits first and moves the version to 1. Session B still thinks the version is 0, so its `UPDATE ... WHERE version = 0` matches nothing and fails. B's stale change is safely rejected instead of silently overwriting A.

## Best practices

- Add `@Version` to any entity edited by multiple users/requests concurrently.
- Catch the conflict and retry or ask the user to reload — don't just crash.
- Prefer optimistic (no locks) over pessimistic for typical web apps; use pessimistic only for genuine hotspots.

## Common mistakes

- Setting the `@Version` field manually (breaks the mechanism).
- Not handling `OptimisticLockException` → users get raw 500 errors on conflicts.
- Assuming it prevents *all* concurrency issues — it prevents lost updates on a single row, not every anomaly.

## Summary

A `@Version` field turns every update into a compare-and-set on the version. Concurrent stale writes fail with `OptimisticLockException`, preventing lost updates — no locks held.

## How this appears in Spring Data JPA

`@Version` works identically — Spring Data JPA fully honors it. Extra Spring conveniences:

- `save()` on a `@Version` entity performs the versioned update automatically.
- Spring surfaces conflicts as `ObjectOptimisticLockingFailureException`, which you can handle globally.
- The version field also lets Spring Data decide *new vs existing* for `save()` (a `null` version ⇒ new entity ⇒ `persist`).

This is the standard concurrency-control tool in real Spring applications, and it starts with the exact `@Version` field you used here.

---
➡️ **Next:** [23 · Performance Tips](23-Performance-Tips.md)
