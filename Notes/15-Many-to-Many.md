# 15 · Many-to-Many

## Introduction

A **many-to-many** relationship links many rows on each side — an `Author` writes many `Book`s and a `Book` can have many `Author`s.

## Why this concept exists

Some relationships genuinely go both ways at scale: students↔courses, actors↔movies, authors↔books. A single foreign key can't express this — you need a **join table**.

## Internal working

Relational databases cannot store a many-to-many directly. Hibernate creates a **join table** with two foreign keys (`author_id`, `book_id`). The **owning** side declares it with `@JoinTable`; the **inverse** side uses `mappedBy`.

```
authors        author_book            books
-------        -------------          -----
id  ◄────────  author_id  book_id  ────────►  id
```

## Important annotations

```java
// OWNING side — declares the join table
@ManyToMany
@JoinTable(
    name = "author_book",
    joinColumns        = @JoinColumn(name = "author_id"),
    inverseJoinColumns = @JoinColumn(name = "book_id"))
private List<Book> books = new ArrayList<>();

// INVERSE side
@ManyToMany(mappedBy = "books")
private List<Author> authors = new ArrayList<>();
```

## Simple example

```java
Author a1 = new Author("Erich Gamma");
Author a2 = new Author("Ralph Johnson");
Book book = new Book("Design Patterns");
a1.addBook(book);   // helper links both sides
a2.addBook(book);   // same book, second author
session.persist(a1);
session.persist(a2);
```

▶️ Runnable: `com.example.relationships.manytomany.ManyToManyDemo`

## Explanation of the example

Both authors reference the same `Book`. Hibernate writes two rows into the `author_book` join table. The `addBook` helper keeps both collections consistent.

## Best practices

- Use `List`/`Set` and a helper to keep both sides in sync.
- Prefer `Set` when duplicates make no sense (it also avoids some delete-and-reinsert behavior).
- **If the link needs its own data** (e.g. `enrolledOn`, `role`), don't use `@ManyToMany` — model the join table as its own entity with two `@ManyToOne`s. This is very common in real systems.
- Keep it LAZY (the default) — join tables can be large.

## Common mistakes

- Adding extra columns to a `@ManyToMany` join table (impossible — promote it to an entity).
- Putting `@JoinTable` on *both* sides (forgetting `mappedBy`) → two join tables.
- `CascadeType.REMOVE` on `@ManyToMany` — deleting an author would try to delete shared books. Avoid.

## Summary

Many-to-many needs a join table. Owning side uses `@JoinTable`; inverse uses `mappedBy`. If the relationship carries its own attributes, model the join table as an entity with two many-to-ones instead.

## How this appears in Spring Data JPA

Same mapping and same join table. In practice, Spring projects very often **avoid raw `@ManyToMany`** and instead create an explicit join entity (e.g. `Enrollment` with `@ManyToOne Student` + `@ManyToOne Course` + extra fields) with its own repository — because real link tables almost always grow attributes. Knowing when to "promote the join table to an entity" is a judgment call that carries directly into Spring data modeling.

---
➡️ **Next:** [16 · Cascade Types](16-Cascade-Types.md)
