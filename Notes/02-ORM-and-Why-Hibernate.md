# 02 · ORM and Why Hibernate

## Introduction

**ORM (Object-Relational Mapping)** is the technique of mapping object-oriented Java to relational tables. It resolves the mismatch between two very different worlds: **objects** (with inheritance, references, collections) and **tables** (rows, columns, foreign keys).

## Why this concept exists

Objects and relational databases don't line up naturally — this is the *object-relational impedance mismatch*:

| Java world | Relational world |
|------------|------------------|
| Class | Table |
| Object (instance) | Row |
| Field / property | Column |
| Reference (`a.getB()`) | Foreign key + JOIN |
| Collection (`List<Employee>`) | One-to-many rows |
| Inheritance | *(no direct equivalent)* |

Writing code to translate between these two by hand (plain JDBC) is tedious and repetitive. ORM automates the translation.

## The problem ORM solves (plain JDBC)

```java
// The old way — for ONE insert:
String sql = "INSERT INTO students (name, course, email) VALUES (?, ?, ?)";
PreparedStatement ps = connection.prepareStatement(sql);
ps.setString(1, student.getName());
ps.setString(2, student.getCourse());
ps.setString(3, student.getEmail());
ps.executeUpdate();
// ...plus connection handling, transactions, ResultSet mapping, exception handling
```

Multiply that by every table and every query in your app.

## How Hibernate fixes it

```java
session.persist(student);   // that's the whole insert
```

Benefits:

- **Less code** — no boilerplate JDBC.
- **Database independence** — switch PostgreSQL ↔ MySQL by changing a dialect, not your queries.
- **Automatic mapping** — rows become objects and back.
- **Caching, dirty checking, lazy loading** — performance features you'd never write by hand.

## Important annotations

Just the two you cannot live without yet — more in later notes:

```java
@Entity                 // "map this class to a table"
public class Student {
    @Id                 // "this field is the primary key"
    private Long id;
}
```

## Simple example

The same `HelloHibernateDemo` from Note 01 is a one-line insert. Compare it mentally to the 8-line JDBC block above — that difference *is* ORM.

## Best practices

- Don't fight the relational model — ORM hides SQL, it doesn't abolish it.
- Prefer standard JPA mapping annotations for portability.

## Common mistakes

- Treating an ORM as a reason to ignore how the database works. The best ORM users are strong at SQL.
- Expecting every object graph to map cleanly; some designs need tuning.

## Summary

ORM bridges the object/table mismatch; Hibernate is the mature, standard Java ORM that automates the translation and adds caching, dirty checking, and lazy loading.

## How this appears in Spring Data JPA

Spring Data JPA *is* ORM with even less ceremony. A method like `studentRepository.save(student)` calls Hibernate's `persist`/`merge` for you. The impedance-mismatch problems (lazy loading, JOINs, N+1) do **not** disappear in Spring — so the ORM understanding you build here is exactly what keeps Spring apps fast.

---
➡️ **Next:** [03 · Hibernate Architecture](03-Hibernate-Architecture.md)
