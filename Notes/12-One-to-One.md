# 12 · One-to-One

## Introduction

A **one-to-one** relationship links exactly one row of table A to one row of table B — e.g. a `Person` and their `Passport`.

## Why this concept exists

Sometimes a concept splits naturally into two tables: a main entity and an optional/heavy detail record (profile, passport, settings). One-to-one models that link.

## Internal working

One side is the **owning** side and holds the foreign key (`@JoinColumn`). The other side is the **inverse** side and uses `mappedBy` to point back — it adds no extra column.

## Important annotations

```java
// OWNING side (holds the FK column passport_id)
@OneToOne(cascade = CascadeType.ALL)
@JoinColumn(name = "passport_id")
private Passport passport;

// INVERSE side (no extra column)
@OneToOne(mappedBy = "passport")
private Person person;
```

## Simple example

```java
Person person = new Person("Nikola Tesla");
person.setPassport(new Passport("P-1234567"));
session.persist(person);   // cascade ALL saves the Passport too
```

▶️ Runnable: `com.example.relationships.onetoone.OneToOneDemo`

## Explanation of the example

`Person` owns the relationship, so its table gets the `passport_id` FK. `cascade = ALL` means persisting the person also persists the passport in one call.

## Best practices

- Put the FK on the side that is "more owning" (here, `Person`).
- Consider making the `@OneToOne` **lazy** — by default it's EAGER, which forces a JOIN/second query even when you don't need the associated entity. (Lazy `@OneToOne` on the inverse side needs care; the owning side lazies more easily.)
- Use `@MapsId` when the two tables should share the same primary key (shared-PK one-to-one) — the most efficient form.

## Common mistakes

- Defining the FK on both sides (forgetting `mappedBy`) → two columns, broken mapping.
- Leaving it EAGER on a hot path and paying for JOINs you don't need.
- Expecting the inverse side to lazy-load easily (it often can't without a shared PK).

## Summary

One-to-one links single rows across two tables. The owning side has the FK + `@JoinColumn`; the inverse side uses `mappedBy`. Watch the default EAGER fetch.

## How this appears in Spring Data JPA

Same annotations, unchanged. In a Spring app you'd have a `PersonRepository`; calling `save(person)` cascades to the passport exactly as here. The main Spring-relevant caveat is the same one: an EAGER `@OneToOne` quietly adds a JOIN to *every* query for that entity, including repository finder methods — a frequent hidden performance cost.

---
➡️ **Next:** [13 · One-to-Many](13-One-to-Many.md)
