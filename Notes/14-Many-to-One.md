# 14 · Many-to-One

## Introduction

A **many-to-one** relationship is the "many" side's view of a one-to-many: many `OrderEntry` rows belong to one `Customer`. It is the **owning** side that physically holds the foreign key, and it is the single most common association mapping you will write.

## Why this concept exists

Most child records point to a parent: an order to a customer, a comment to a post, an employee to a department. `@ManyToOne` is how the child references its parent — and it's often all you need (a *unidirectional* many-to-one), without the parent holding a collection at all.

## Internal working

The `@ManyToOne` field maps to a foreign-key column (`@JoinColumn`) on the child's table. Loading the child can load the parent — **eagerly by default**, which is a frequent performance trap.

## Important annotations

```java
@ManyToOne(fetch = FetchType.LAZY)   // override the EAGER default!
@JoinColumn(name = "customer_id")    // FK column on the order_entries table
private Customer customer;
```

## Simple example

```java
Customer customer = new Customer("Acme Corp");
session.persist(customer);
session.persist(new OrderEntry("Keyboard", customer));
session.persist(new OrderEntry("Monitor",  customer));
// two order rows share one customer_id
```

▶️ Runnable: `com.example.relationships.manytoone.ManyToOneDemo`

## Explanation of the example

Each `OrderEntry` stores the customer's id in its `customer_id` column. This is a **unidirectional** many-to-one: `Customer` doesn't even know about its orders, which keeps the model lean. Add a `@OneToMany(mappedBy="customer")` on `Customer` only if you actually need to navigate from customer → orders.

## `@ManyToOne` default fetch = EAGER (important!)

Unlike `@OneToMany`/`@ManyToMany` (LAZY by default), **`@ManyToOne` and `@OneToOne` default to EAGER**. That means every time you load the child, Hibernate also loads the parent — even when you never use it. Almost always set `fetch = FetchType.LAZY` explicitly.

## Best practices

- Set `@ManyToOne(fetch = LAZY)` unless you truly always need the parent.
- Prefer a unidirectional `@ManyToOne` when you don't need the reverse collection — it's the simplest, most efficient mapping.
- Use `getReference(Parent.class, id)` to set the FK without loading the parent.

## Common mistakes

- Leaving the EAGER default and triggering extra SELECTs / JOINs everywhere.
- Making the relationship bidirectional "just in case," adding a collection nobody uses.
- Forgetting `@JoinColumn`, letting Hibernate pick a default column name you didn't expect.

## Summary

`@ManyToOne` is the owning side that holds the FK column. It defaults to EAGER — override to LAZY. A unidirectional many-to-one is often the cleanest relationship mapping.

## How this appears in Spring Data JPA

Unchanged annotations. The EAGER default is a top cause of surprise queries in Spring apps: a repository `findAll()` on the child silently JOINs/loads every parent. Fixing it is a one-word change (`fetch = LAZY`) plus `JOIN FETCH`/`@EntityGraph` where you *do* need the parent. This note's rule — "`@ManyToOne` should almost always be LAZY" — is one of the highest-value habits to carry into Spring Data JPA.

---
➡️ **Next:** [15 · Many-to-Many](15-Many-to-Many.md)
