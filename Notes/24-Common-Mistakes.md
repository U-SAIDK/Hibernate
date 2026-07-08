# 24 · Common Mistakes

## Introduction

A consolidated checklist of the mistakes that bite almost every Hibernate (and therefore Spring Data JPA) beginner. If something isn't working, scan this list first.

## Why this concept exists

The same handful of misunderstandings cause the majority of Hibernate bugs. Seeing them collected — with the fix — saves hours of debugging.

## The mistakes (and fixes)

### 1. Modifying a detached entity and expecting it to save
Changes are only auto-saved for **persistent** entities (Note 07). Reattach with `merge()` or mutate inside the transaction.

### 2. Leaving `@ManyToOne` / `@OneToOne` EAGER
They default to EAGER (Note 17). Every load drags in the association. **Set them LAZY.**

### 3. The N+1 query problem
Iterating parents and touching lazy children → 1+N queries (Note 23). Fix with `join fetch` / entity graph.

### 4. `LazyInitializationException`
Touching a lazy field after the session/transaction closed. Fetch it inside the transaction, or use a DTO/entity graph — **don't** switch to EAGER.

### 5. Forgetting the no-arg constructor
Hibernate instantiates via reflection and needs it (Note 06).

### 6. Not keeping both sides of a bidirectional relationship in sync
Update only the owning (FK) side, or use a helper that sets both (Note 13).

### 7. `hbm2ddl.auto = update` (or `create`) in production
Can silently drift or destroy schema (Note 04). Use `validate` + Flyway/Liquibase.

### 8. Redundant `save()` after changing a managed entity
Dirty checking already handles it (Note 20). Harmless but unnecessary.

### 9. `@Enumerated(ORDINAL)` (the default)
Reordering the enum corrupts existing data. Use `@Enumerated(EnumType.STRING)` (Note 06).

### 10. Cascading `REMOVE` across shared/many-to-many references
Deletes entities other parents still need (Note 16). Cascade downward only.

### 11. Using `equals()`/`hashCode()` based on a generated id
Before persist the id is `null`; it changes after insert, breaking `Set` membership. Prefer a business key or be very careful.

### 12. Pagination without `order by`
Nondeterministic pages (Note 19). Always sort, ideally by a unique column.

### 13. Long-running sessions / huge persistence context
Memory bloat. Keep sessions short; `flush()`+`clear()` in batch loops (Note 21).

## Summary

Most Hibernate bugs come from entity state confusion, EAGER defaults, N+1, and schema mismanagement. Learn to recognize these thirteen and you'll debug fast.

## How this appears in Spring Data JPA

**Every single one of these happens in Spring Data JPA too** — same JPA/Hibernate underneath. The most frequent Spring versions:

- `LazyInitializationException` in controllers (serializing entities after the transaction).
- N+1 in list endpoints (fix with `@EntityGraph`).
- `ddl-auto` set wrong for the environment.
- Redundant `save()` calls in `@Transactional` services.

This checklist is, in effect, a Spring Data JPA debugging guide.

---
➡️ **Next:** [25 · Hibernate to Spring Data JPA](25-Hibernate-to-Spring-Data-JPA.md)
