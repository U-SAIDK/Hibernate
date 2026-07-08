# 10 · Transactions

## Introduction

A **transaction** is a group of database operations that succeed or fail **as one unit**. Either everything commits, or everything rolls back. In Hibernate every write must happen inside a transaction.

## Why this concept exists

Real operations touch multiple rows (transfer money = debit one account + credit another). If the second step fails, the first must be undone or your data is corrupt. Transactions provide that all-or-nothing guarantee.

## ACID in one line each

- **Atomicity** — all steps happen, or none do.
- **Consistency** — the database moves from one valid state to another.
- **Isolation** — concurrent transactions don't see each other's uncommitted changes.
- **Durability** — once committed, changes survive a crash.

## Internal working

`beginTransaction()` gets a JDBC connection and turns off auto-commit. Your persist/merge/remove calls are staged. `commit()` **flushes** the pending SQL and commits the JDBC transaction. `rollback()` discards everything since `begin`.

## Simple example

```java
Transaction tx = session.beginTransaction();
try {
    session.persist(new Student("Alan Turing", "Logic", "alan@ex.com"));
    if (somethingWrong) throw new IllegalStateException("fail!");
    session.persist(new Student("Second", "X", "second@ex.com"));
    tx.commit();                       // both saved together
} catch (RuntimeException ex) {
    tx.rollback();                     // neither saved
}
```

▶️ Runnable: `com.example.transactions.TransactionDemo`

## Explanation of the example

The exception fires after the first `persist`. Because we `rollback()`, even that first insert is undone — the table is left untouched. That's atomicity.

## Best practices

- One transaction = one unit of work. Keep it short.
- Always `rollback()` on exception (or use a try/catch/finally pattern).
- Don't do slow work (HTTP calls, file IO) inside a transaction — it holds the DB connection.
- Read-only work can run in a read-only transaction for a small optimization.

## Common mistakes

- Forgetting to commit → changes silently lost.
- Not rolling back on error → connection left in a bad state / inconsistent data.
- Wrapping way too much work in one transaction → lock contention.

## Summary

A transaction makes multiple operations atomic. Begin, do the work, commit on success or rollback on failure — always inside a transaction for writes.

## How this appears in Spring Data JPA

You almost never write `beginTransaction`/`commit`/`rollback` in Spring. You annotate:

```java
@Transactional
public void enroll(Long studentId, String course) {
    Student s = studentRepository.findById(studentId).orElseThrow();
    s.setCourse(course);              // dirty checking saves it
}                                     // Spring commits here (rolls back on RuntimeException)
```

Spring's `@Transactional` runs the exact begin/commit/rollback pattern around your method. By default it rolls back on unchecked (`RuntimeException`) exceptions. It also defines the persistence-context boundary — so entities are managed *inside* the method and detached after it.

---
➡️ **Next:** [11 · Primary Key Generation](11-Primary-Key-Generation.md)
