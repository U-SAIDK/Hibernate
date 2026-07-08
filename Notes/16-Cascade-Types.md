# 16 · Cascade Types

## Introduction

**Cascading** propagates an operation from a parent entity to its associated entities. Persist a `Department` and its `Employee`s are persisted too — because you cascaded the persist.

## Why this concept exists

Without cascade you'd have to manually `persist()`/`remove()` every associated entity in the right order. Cascade lets you operate on an object graph as a unit: save the parent, and the children come along.

## The cascade types

| Cascade type | Propagates… |
|--------------|-------------|
| `PERSIST` | `persist()` — save children when parent is saved |
| `MERGE` | `merge()` |
| `REMOVE` | `remove()` — delete children when parent is deleted |
| `REFRESH` | `refresh()` |
| `DETACH` | `detach()` |
| `ALL` | all of the above |

Plus a related (but separate) flag:

- **`orphanRemoval = true`** — when a child is *removed from the parent's collection*, delete its row. Different from `REMOVE`, which only fires when the *parent* is deleted.

## Important annotations

```java
@OneToMany(mappedBy = "department",
           cascade = CascadeType.ALL,
           orphanRemoval = true)
private List<Employee> employees = new ArrayList<>();
```

## Simple example

```java
Department dept = new Department("Engineering");
dept.addEmployee(new Employee("Dennis Ritchie"));
session.persist(dept);   // cascade PERSIST also inserts the employee
```

▶️ Runnable: `com.example.relationships.onetomany.OneToManyDemo`

## Explanation of the example

You only persist `dept`. Because the association is `cascade = ALL`, Hibernate also persists each `Employee`. Deleting the department would (with `REMOVE`) delete its employees too.

## Cascade vs orphanRemoval

- `CascadeType.REMOVE` — deleting the **parent** deletes the children.
- `orphanRemoval = true` — removing a child **from the collection** deletes that child, even if the parent lives on.

Use both together for strict parent-owns-children lifecycles.

## Best practices

- Use `cascade = ALL` + `orphanRemoval` for genuine parent→child ownership (order → line items).
- **Do NOT** cascade `REMOVE` across `@ManyToMany` or shared references — you'll delete entities other parents still use.
- Be conservative: only cascade where the child truly cannot exist without the parent.

## Common mistakes

- `CascadeType.ALL` on a `@ManyToOne` (child → parent): deleting a child could delete the shared parent. Cascade flows parent → child, not the reverse.
- Expecting removing an item from a list to delete it *without* `orphanRemoval`.
- Cascading remove onto shared entities.

## Summary

Cascade propagates operations from parent to children. `ALL` covers everything; `orphanRemoval` deletes children pulled out of the collection. Cascade downward (parent→child) and never across shared references.

## How this appears in Spring Data JPA

Cascade is a JPA mapping concept, so it behaves identically — `repository.save(parent)` cascades to children, `delete(parent)` cascades removes. Spring adds no cascade of its own; a common beginner bug is expecting `deleteById` to remove children when no `CascadeType.REMOVE`/`orphanRemoval` is configured. What you set on the entity here is exactly what governs Spring's behavior.

---
➡️ **Next:** [17 · Fetch Types](17-Fetch-Types.md)
