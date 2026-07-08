# 23 · Performance Tips

## Introduction

Hibernate is fast *if you use it well*. Most "Hibernate is slow" complaints are really "I let Hibernate run 500 queries when 1 would do." This note collects the highest-impact habits.

## Why this concept exists

The convenience of an ORM makes it easy to trigger huge amounts of hidden SQL. Knowing the traps — and their fixes — is what separates a working app from a fast one.

## The #1 problem: N+1 queries

Loading N parents, then touching each one's lazy association, fires **1 + N** queries:

```java
List<Employee> emps = session.createQuery("from Employee", Employee.class).getResultList(); // 1
for (Employee e : emps) {
    System.out.println(e.getDepartment().getName());   // +1 SELECT per employee → N more
}
```

**Fixes:**
- `join fetch`: `from Employee e join fetch e.department` → one query.
- An **entity graph** to declare what to load.
- Batch fetching (`@BatchSize`) to turn N selects into N/batch.

## The performance checklist

| Tip | Why |
|-----|-----|
| Make associations **LAZY** | Avoid loading unused data (Note 17) |
| Fix **N+1** with `join fetch` / entity graphs | Turn 1+N queries into 1 |
| Use **projections/DTOs** for read screens | Don't hydrate whole entities to show 3 fields |
| **Paginate** large results | Never `getResultList()` a whole table (Note 19) |
| Use **`SEQUENCE`** + `hibernate.jdbc.batch_size` | Enables batch inserts (Note 11) |
| **`flush()` + `clear()`** in big loops | Keep the persistence context small |
| Keep transactions **short** | Release connections/locks quickly |
| Turn **`show_sql` off** in production | Logging overhead + noise |
| Add proper **database indexes** | The ORM can't fix a missing index |

## Simple example — fixing N+1

```jpql
-- before: 1 + N queries
select e from Employee e
-- after: 1 query
select e from Employee e join fetch e.department
```

## Best practices

- **Watch the SQL log** during development — count the queries a page produces.
- Optimize the query, not the mapping: keep mappings LAZY, fetch eagerly *per use case*.
- Prefer read-only DTO projections for reporting/list views.

## Common mistakes

- "Fixing" N+1 by making associations EAGER (creates N+1 or Cartesian products elsewhere).
- Loading entire tables into memory.
- `IDENTITY` ids on bulk-insert tables (no batching).
- Ignoring database indexes and blaming Hibernate.

## Summary

The big wins: LAZY by default, kill N+1 with fetch joins/entity graphs, use DTO projections, paginate, batch inserts with `SEQUENCE`, keep the context and transactions small, and index your database.

## How this appears in Spring Data JPA

Every tip maps directly to a Spring feature:

- N+1 → `@EntityGraph` on the repository method, or `JOIN FETCH` in `@Query`.
- Projections → Spring Data **interface/DTO projections** and constructor expressions.
- Pagination → `Pageable`.
- Batch inserts → `spring.jpa.properties.hibernate.jdbc.batch_size` + `SEQUENCE` ids.
- `open-in-view=false` to expose lazy issues early instead of hiding them.

The performance work in a Spring app *is* Hibernate tuning — this note is your field guide.

---
➡️ **Next:** [24 · Common Mistakes](24-Common-Mistakes.md)
