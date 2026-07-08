# 19 · Pagination and Sorting

## Introduction

**Pagination** returns results in fixed-size chunks (pages) instead of all at once; **sorting** orders them. Together they let you show "page 3, 20 per page, newest first" without loading the whole table.

## Why this concept exists

A table can have millions of rows. Loading them all wastes memory, kills response time, and is pointless — a user sees 20 at a time. Pagination fetches only the slice you need.

## Internal working

- `setFirstResult(offset)` → SQL `OFFSET`
- `setMaxResults(size)` → SQL `LIMIT`
- `order by ...` in the query → SQL `ORDER BY`

Page number → offset: `offset = pageNumber * pageSize`.

## Important API

```java
int page = 2, size = 20;
List<Student> pageContent = session.createQuery(
        "from Student s order by s.name", Student.class)
    .setFirstResult(page * size)   // OFFSET 40
    .setMaxResults(size)           // LIMIT 20
    .getResultList();
```

## Simple example

▶️ Runnable: `com.example.pagination.PaginationDemo` — seeds 5 rows and walks through them 2 per page.

## Explanation of the example

Each loop iteration asks for a window (`OFFSET page*size LIMIT size`) of an **ordered** query. Ordering matters: without `order by`, "page 2" is undefined because the database may return rows in any order.

## Best practices

- **Always** pair pagination with a stable `order by` — otherwise pages overlap or skip rows.
- Order by something unique (or add the id as a tiebreaker) for deterministic paging.
- For huge offsets, prefer **keyset (seek) pagination** (`where id > :lastId order by id limit :size`) — `OFFSET` gets slow because the DB still scans skipped rows.
- Fetch a separate `count(*)` only when you need total pages.

## Common mistakes

- Paginating without sorting → nondeterministic pages.
- Deep `OFFSET` (page 10,000) → slow queries.
- `setMaxResults` together with `join fetch` on a **collection** → Hibernate paginates in memory (loads everything, warns in the log). Paginate the root entity, fetch collections separately.

## Summary

Pagination = `OFFSET` (`setFirstResult`) + `LIMIT` (`setMaxResults`), always with an `ORDER BY`. For very large datasets prefer keyset pagination over deep offsets.

## How this appears in Spring Data JPA

Spring makes this first-class with `Pageable` and `Sort`:

```java
Page<Student> page = studentRepository.findAll(
        PageRequest.of(2, 20, Sort.by("name")));
```

`PageRequest.of(page, size, sort)` becomes exactly the `setFirstResult`/`setMaxResults`/`order by` you wrote by hand here. A `Page` also runs the `count` query for total pages. The in-memory-pagination-with-collection-fetch warning is identical in Spring — so the habits from this note carry straight over.

---
➡️ **Next:** [20 · Dirty Checking](20-Dirty-Checking.md)
