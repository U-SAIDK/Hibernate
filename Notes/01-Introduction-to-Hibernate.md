# 01 · Introduction to Hibernate

> **Goal of this course:** learn *just enough* modern Hibernate to walk into Spring Data JPA and understand what it is doing for you under the hood.

---

## Introduction

**Hibernate** is a Java framework that saves your Java objects into a relational database (and loads them back) **without you writing SQL by hand**. It is the most widely used implementation of the **Jakarta Persistence API (JPA)**.

You work with objects. Hibernate generates the SQL, talks to the database over JDBC, and turns rows back into objects.

```
Your Java object  ──►  Hibernate  ──►  SQL  ──►  JDBC  ──►  Database
```

## Why this concept exists

Before ORM, persisting an object meant writing JDBC by hand:

- Open a connection, write the `INSERT`, set every `?` parameter.
- Run the query, read the `ResultSet` column by column.
- Map each column back onto a Java field.
- Handle transactions and close everything in `finally`.

This is repetitive, error-prone, and database-specific. Hibernate removes almost all of it.

## Internal working (the 10,000-ft view)

1. You describe your classes with annotations (`@Entity`, `@Id`, …).
2. At startup Hibernate reads that metadata and builds a `SessionFactory`.
3. You open a `Session`, ask it to save/find/delete objects.
4. Hibernate generates the SQL, executes it through JDBC, and manages the transaction.

## Important terms

| Term | Meaning |
|------|---------|
| **ORM** | Object-Relational Mapping — mapping objects ⇄ tables |
| **JPA** | The *specification* (a set of interfaces + annotations) |
| **Hibernate** | The most popular *implementation* of JPA |
| **Entity** | A Java class mapped to a database table |

## Simple example

```java
Student student = new Student("Ada Lovelace", "Computer Science", "ada@example.com");
session.persist(student);   // Hibernate writes the INSERT for you
```

▶️ Runnable: `com.example.introduction.HelloHibernateDemo`

## Explanation of the example

You never wrote `INSERT INTO students ...`. You created an object and handed it to Hibernate. Turn on `show_sql` and you will see Hibernate emit the SQL itself.

## Best practices

- Learn JPA annotations (`jakarta.persistence.*`), not Hibernate-only ones — they carry straight over to Spring.
- Keep `show_sql` / `format_sql` on while learning so you *see* what Hibernate does.

## Common mistakes

- Thinking Hibernate replaces SQL knowledge. It doesn't — you still need to understand the SQL it generates.
- Confusing JPA (the spec) with Hibernate (the implementation).

## Summary

Hibernate is a JPA implementation that maps objects to tables so you manipulate data as Java objects instead of writing SQL.

## How this appears in Spring Data JPA

Spring Data JPA is a **thin layer on top of JPA/Hibernate**. When you write a Spring `@Repository`, Spring Boot uses Hibernate as the default JPA provider underneath. Every concept in this course is still running — Spring just hides the boilerplate (no `SessionFactory`, no `openSession`, no manual `beginTransaction`). Understanding Hibernate first means Spring Data JPA becomes "the same thing, less code."

---
➡️ **Next:** [02 · ORM and Why Hibernate](02-ORM-and-Why-Hibernate.md)
