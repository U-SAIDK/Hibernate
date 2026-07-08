# 06 · Entities

## Introduction

An **entity** is a plain Java class that Hibernate maps to a database table. Each instance is a row; each mapped field is a column.

## Why this concept exists

The entity is the *unit of mapping* — it's how you describe, in Java, what a table looks like and how its columns relate to your fields. Everything else (CRUD, queries, relationships) operates on entities.

## Rules an entity must follow

1. Annotated with `@Entity`.
2. Has a primary-key field marked `@Id`.
3. Has a **no-argument constructor** (Hibernate instantiates via reflection).
4. Is a non-final class with accessible fields/properties.

## Important annotations

| Annotation | Purpose |
|------------|---------|
| `@Entity` | Marks the class as a mapped entity |
| `@Table(name="...")` | Sets the table name (defaults to class name) |
| `@Id` | Marks the primary key |
| `@GeneratedValue` | Auto-generates the id (see Note 11) |
| `@Column(name, nullable, length, unique)` | Customizes a column |
| `@Transient` | A field that is **not** persisted |
| `@Enumerated(EnumType.STRING)` | Store enums as text (always prefer STRING) |

## Simple example

```java
@Entity
@Table(name = "students")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor   // Lombok
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    private String course;

    @Column(unique = true)
    private String email;
}
```

▶️ Source: `com.example.model.Student`

## Explanation of the example

- `@Entity` + `@Table` → this class maps to the `students` table.
- `@Id` + `@GeneratedValue(IDENTITY)` → the database assigns the primary key.
- `@Column(nullable=false)` → `NOT NULL`; `unique=true` → a unique constraint.
- Unannotated fields (`course`) still map to columns with defaults.
- Lombok generates the getters/setters/constructors so the class stays readable.

## Best practices

- Use `Long` (wrapper) for ids, not `long`, so `null` means "not yet persisted".
- Prefer wrapper types for nullable columns.
- Store enums with `@Enumerated(EnumType.STRING)` — ordinal storage breaks when you reorder the enum.
- Keep entities as data + mapping; keep business logic in services.

## Common mistakes

- Forgetting the no-arg constructor → Hibernate can't instantiate the entity.
- Missing `@Id`.
- Using `@Enumerated(ORDINAL)` (the default) and later reordering the enum → corrupted data.
- Putting heavy logic or non-persistent state in entities without `@Transient`.

## Summary

An entity is a `@Entity`-annotated POJO with an `@Id` and a no-arg constructor; its fields map to columns and its instances map to rows.

## How this appears in Spring Data JPA

**Identical.** Entities in Spring Data JPA use the exact same `jakarta.persistence` annotations — there is no "Spring entity". The only difference is Spring Boot auto-discovers them by classpath scanning instead of you registering each in XML. Everything you learn here about `@Entity`, `@Id`, `@Column`, `@GeneratedValue` transfers 1:1.

---
➡️ **Next:** [07 · Entity Lifecycle](07-Entity-Lifecycle.md)
