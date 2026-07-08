# 13 · One-to-Many

## Introduction

A **one-to-many** relationship links one row to many rows — one `Department` has many `Employee`s. It is the most common relationship in real applications, and it is the mirror image of many-to-one (Note 14).

## Why this concept exists

Parent/child and owner/items structures are everywhere: a department's employees, an order's line items, a post's comments. One-to-many models the "one parent, many children" shape.

## Internal working

In a relational database the **foreign key always lives on the "many" side** (the `employees` table has a `department_id` column). Therefore, in a bidirectional mapping, the **many** side (`@ManyToOne`) is the *owning* side, and the **one** side (`@OneToMany`) is the *inverse* side (`mappedBy`).

> Rule of thumb: `mappedBy` goes on the `@OneToMany`. The `@ManyToOne` side owns the FK.

## Important annotations

```java
// ONE side (inverse) — no FK column here
@OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Employee> employees = new ArrayList<>();

// MANY side (owning) — holds the FK, see Note 14
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "department_id")
private Department department;
```

## Simple example

```java
Department dept = new Department("Engineering");
dept.addEmployee(new Employee("Dennis Ritchie"));   // helper sets BOTH sides
dept.addEmployee(new Employee("Ken Thompson"));
session.persist(dept);                               // cascade inserts both employees
```

▶️ Runnable: `com.example.relationships.onetomany.OneToManyDemo`

## Explanation of the example

The `addEmployee` helper adds to the list **and** sets `employee.setDepartment(this)`. Keeping both sides in sync is essential — Hibernate persists the FK based on the *owning* (`@ManyToOne`) side.

## Best practices

- Default `@OneToMany` fetch is LAZY — keep it that way.
- Always use a **helper method** to keep both sides of a bidirectional link consistent.
- Use `cascade = ALL` + `orphanRemoval = true` for true parent→child ownership (removing a child from the list deletes its row).
- Initialize collections (`= new ArrayList<>()`) to avoid `NullPointerException`.

## Common mistakes

- Updating only the collection and forgetting to set the `@ManyToOne` back-reference → FK not saved.
- Making `@OneToMany` EAGER → loads potentially huge collections on every parent fetch.
- A **unidirectional** `@OneToMany` without `@JoinColumn` → Hibernate creates an extra join table (usually not what you want).

## Summary

One-to-many = one parent, many children. The FK lives on the many side, which owns the relationship; the one side is `mappedBy`. Keep both sides in sync and keep the collection lazy.

## How this appears in Spring Data JPA

Same mapping. The big Spring-relevant consequence is the **N+1 problem** (Note 23): iterating parents and touching each one's lazy children fires one query per parent. In Spring you solve it exactly as in Hibernate — a `@Query` with `JOIN FETCH` or an `@EntityGraph` on the repository method. Also, `cascade`/`orphanRemoval` behave identically when you `save()` a parent through a repository.

---
➡️ **Next:** [14 · Many-to-One](14-Many-to-One.md)
