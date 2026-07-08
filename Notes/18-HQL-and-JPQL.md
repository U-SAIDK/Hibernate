# 18 · HQL and JPQL

## Introduction

**JPQL** (Jakarta Persistence Query Language) and **HQL** (Hibernate Query Language) are object-oriented query languages. You query **entities and their fields**, not tables and columns. JPQL is the standard; HQL is Hibernate's superset — for everyday use they're the same.

## Why this concept exists

`find(id)` only fetches by primary key. Real apps need "all students in Physics", "count of orders per customer", "names ordered alphabetically". These need a query language — but one that speaks *objects*, so results come back as entities.

## HQL/JPQL vs SQL

```jpql
-- JPQL: entity names and fields
from Student s where s.course = :course
```
```sql
-- the SQL Hibernate generates
select * from students where course = ?
```

Note `Student` (the class) not `students` (the table), and `s.course` (the field) not the column. Hibernate translates.

## Core syntax

```jpql
-- SELECT entities
select s from Student s where s.course = :course order by s.name

-- Projection (specific fields)
select s.name from Student s

-- Aggregation
select count(s) from Student s

-- Joins across associations
select e from Employee e join e.department d where d.name = :dept

-- Fetch join (load association in one query — fixes N+1)
select d from Department d join fetch d.employees
```

## Important API

```java
List<Student> list = session.createQuery(
        "from Student s where s.course = :course", Student.class)
    .setParameter("course", "Physics")   // ALWAYS use parameters, never string concat
    .getResultList();

Long n = session.createQuery("select count(s) from Student s", Long.class)
    .getSingleResult();
```

## Simple example

▶️ Runnable: `com.example.hql.HqlDemo` and `com.example.jpql.JpqlDemo`.

## Explanation of the example

`HqlDemo` runs a filtered select, a projection, and a count. Every filter uses a **named parameter** (`:course`) so values are bound safely — never concatenated into the query string.

## Best practices

- **Always** use bind parameters (`:name`) — string concatenation invites SQL injection.
- Select only what you need; use projections/DTOs for read-heavy screens.
- Use `join fetch` to load associations you'll need, avoiding N+1.
- Type your queries (`createQuery(hql, Student.class)`).

## Common mistakes

- Using table/column names instead of entity/field names.
- Concatenating user input into the query (injection risk).
- `join fetch` on multiple collections → Cartesian explosion.
- Selecting whole entities when you only need two columns.

## Summary

JPQL/HQL query entities and fields, not tables/columns; Hibernate translates them to SQL. Use bind parameters, projections, and `join fetch` deliberately.

## How this appears in Spring Data JPA

This is the language behind Spring's `@Query`:

```java
@Query("select s from Student s where s.course = :course")
List<Student> findByCourse(@Param("course") String course);
```

And **derived query methods** (`findByCourse(String course)`) generate this same JPQL for you from the method name. `JOIN FETCH` inside a `@Query`, or `@EntityGraph`, is how you solve N+1 at the repository level. Everything you learn here is exactly what runs inside a Spring repository.

---
➡️ **Next:** [19 · Pagination and Sorting](19-Pagination-and-Sorting.md)
