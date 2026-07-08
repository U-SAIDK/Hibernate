# 25 · Hibernate to Spring Data JPA

> The finish line. This note ties the whole course together and shows exactly what Spring Data JPA adds — and what it *doesn't* change.

## Introduction

**Spring Data JPA** is a layer **on top of** JPA/Hibernate. It does not replace Hibernate — by default it *uses* Hibernate as its JPA provider. It removes boilerplate (no `SessionFactory`, no `openSession`, no manual transactions, no hand-written CRUD) while every concept you learned still runs underneath.

## The layer cake

```
        Your @Repository / @Service
                  │
        Spring Data JPA        ← generates repository implementations
                  │
        JPA (jakarta.persistence)  ← the spec: @Entity, EntityManager
                  │
        Hibernate              ← the implementation (everything in this course)
                  │
        JDBC → Database
```

## What Spring replaces (boilerplate you now understand)

| You wrote by hand (Hibernate) | Spring does it for you |
|-------------------------------|------------------------|
| `HibernateUtil` / `buildSessionFactory()` | Auto-configured `EntityManagerFactory` bean |
| `openSession()` / `close()` | Injected, transaction-scoped `EntityManager` |
| `beginTransaction` / `commit` / `rollback` | `@Transactional` |
| `hibernate.cfg.xml` + `<mapping>` per entity | `application.properties` + classpath scanning |
| `persist` / `find` / `merge` / `remove` | `JpaRepository` methods (`save`, `findById`, `delete`) |
| Writing HQL for every lookup | Derived query methods (`findByCourse`) |

## The repository — the whole point

```java
public interface StudentRepository extends JpaRepository<Student, Long> {
    // Inherited for free: save, findById, findAll, delete, count, paging, sorting...

    // Derived query — Spring generates the JPQL from the method name:
    List<Student> findByCourse(String course);

    // Custom JPQL when you need it:
    @Query("select s from Student s where s.email like %:domain")
    List<Student> findByEmailDomain(@Param("domain") String domain);
}
```

That interface — **no implementation class** — replaces all the CRUD/query code you wrote in this course. Spring generates the implementation at runtime, delegating to Hibernate.

## Concept-by-concept carryover

| Course note | Still true in Spring Data JPA |
|-------------|-------------------------------|
| Entities (06) | Identical `@Entity`/`@Id`/`@Column` annotations |
| Entity lifecycle (07) | Same 4 states; `save()` = `persist`/`merge` |
| Persistence context (08) | One per `@Transactional` method |
| CRUD (09) | `JpaRepository` methods wrap `persist/find/merge/remove` |
| Transactions (10) | `@Transactional` |
| Id generation (11) | Same `@GeneratedValue`; drives `saveAll` batching |
| Relationships (12–15) | Same annotations, unchanged |
| Cascade (16) | Same; governs `save`/`delete` propagation |
| **Fetch types (17)** | Same defaults; source of `LazyInitializationException` + N+1 |
| HQL/JPQL (18) | Powers `@Query` and derived methods |
| Pagination/sorting (19) | `Pageable` / `Sort` / `Page` |
| Dirty checking (20) | Why you often don't call `save()` |
| First-level cache (21) | One cache per transactional method |
| Optimistic locking (22) | Same `@Version`; `ObjectOptimisticLockingFailureException` |
| Performance (23) | `@EntityGraph`, projections, batch size |

## What to be careful about in Spring (because you now understand why)

- **Lazy loading + web layer** → `LazyInitializationException`. Fix with entity graphs / DTO projections, not global EAGER. Consider `spring.jpa.open-in-view=false`.
- **N+1** in list endpoints → `@EntityGraph` or `JOIN FETCH`.
- **`ddl-auto`** → `validate` + Flyway in real projects.
- **`save()` is `persist` OR `merge`** → depends on whether the entity is new (id/`@Version` null).

## Your next steps

1. Create a Spring Boot project (Spring Web + Spring Data JPA + PostgreSQL) on start.spring.io.
2. Move the `Student` entity across **unchanged**.
3. Replace `CrudDemo` with a `StudentRepository extends JpaRepository<Student, Long>`.
4. Delete `HibernateUtil` and `hibernate.cfg.xml` — `application.properties` + Spring Boot replace them.
5. Wrap service methods in `@Transactional` and watch dirty checking save changes with no `save()` call.

## Summary

Spring Data JPA = JPA/Hibernate + convenience. It deletes the boilerplate you practiced here and keeps every core concept intact. Because you learned Hibernate first, Spring Data JPA is not magic — it's the same machine with a smaller steering wheel.

🎓 **You're ready. Go build with Spring Data JPA.**

---
⬅️ Back to [README](../README.md) · [01 · Introduction](01-Introduction-to-Hibernate.md)
