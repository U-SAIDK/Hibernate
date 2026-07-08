# 17 · Fetch Types

## Introduction

**Fetch type** controls *when* an association is loaded from the database: immediately (**EAGER**) or only when you first access it (**LAZY**). This single setting has a bigger performance impact than almost anything else in Hibernate.

## Why this concept exists

If Hibernate always loaded the entire object graph, loading one `Department` might drag in every employee, every employee's projects, and so on. Lazy loading lets you load only what you need, when you need it.

## EAGER vs LAZY

| | EAGER | LAZY |
|--|-------|------|
| When loaded | Immediately, with the parent | On first access of the field |
| Mechanism | JOIN or extra SELECT up front | Proxy; SELECT fires on access |
| Risk | Loads too much, N+1, slow | `LazyInitializationException` if session closed |

## The defaults (memorize these)

| Association | Default fetch |
|-------------|---------------|
| `@ManyToOne` | **EAGER** ⚠️ |
| `@OneToOne` | **EAGER** ⚠️ |
| `@OneToMany` | LAZY |
| `@ManyToMany` | LAZY |

> The two "to-one" associations default to EAGER — usually the wrong choice. Set them LAZY explicitly.

## Important annotations

```java
@ManyToOne(fetch = FetchType.LAZY)     // override the EAGER default
private Department department;

@OneToMany(mappedBy = "department")    // already LAZY by default
private List<Employee> employees;
```

## Simple example

```java
Employee e = session.find(Employee.class, 1L);   // does NOT load the department (LAZY)
System.out.println(e.getName());                 // still no department query
System.out.println(e.getDepartment().getName()); // NOW the department SELECT fires
```

▶️ Relevant source: `com.example.relationships.onetomany.Employee` (LAZY `@ManyToOne`).

## The LazyInitializationException trap

A lazy field can only be loaded while its session is open. If you return an entity, close the session/transaction, and *then* touch a lazy association, Hibernate throws **`LazyInitializationException`**. Fixes:

- Access the association *inside* the transaction, or
- Fetch it explicitly with `JOIN FETCH` / an entity graph, or
- Map it to a DTO/projection while the session is open.

(The anti-pattern "just make it EAGER" trades one problem for N+1 queries.)

## Best practices

- Make everything LAZY by default; fetch eagerly *per query* when you need it (`JOIN FETCH`).
- Never fix `LazyInitializationException` by switching to EAGER globally.
- Load exactly the data a use case needs — no more.

## Common mistakes

- Leaving `@ManyToOne`/`@OneToOne` EAGER and paying for it on every query.
- `JOIN FETCH`-ing multiple collections at once (Cartesian product blow-up).
- Accessing lazy fields after the transaction closed.

## Summary

LAZY loads on demand; EAGER loads immediately. To-one associations default to EAGER (override to LAZY); collections default to LAZY. Fetch eagerly per-query, not per-mapping.

## How this appears in Spring Data JPA

This is arguably the **most important** Hibernate topic for Spring developers:

- The famous **`LazyInitializationException`** in Spring MVC happens when a controller serializes an entity *after* the `@Transactional` service method returned and the session closed. Solutions: `@EntityGraph` on the repository method, a `JOIN FETCH` `@Query`, or (best) returning a **DTO projection**.
- `spring.jpa.open-in-view` (on by default) papers over this by keeping the session open during rendering — widely considered an anti-pattern; understanding fetch types lets you turn it off safely.

Master EAGER vs LAZY here and you'll debug 80% of real Spring Data JPA performance issues.

---
➡️ **Next:** [18 · HQL and JPQL](18-HQL-and-JPQL.md)
