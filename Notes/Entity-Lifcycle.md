# 🌿 Entity Lifecycle in Hibernate / JPA

<div align="center">

# 📚 Entity Lifecycle

### *Understanding the Complete Journey of an Entity Object*

> **"An Entity is not just a Java object. It goes through different states while Hibernate manages its interaction with the database."**

---

![JPA](https://img.shields.io/badge/JPA-Entity-blue?style=for-the-badge)
![Hibernate](https://img.shields.io/badge/Hibernate-ORM-orange?style=for-the-badge)
![Java](https://img.shields.io/badge/Java-POJO-red?style=for-the-badge)

</div>

---

# 📖 What is an Entity?

An **Entity** is a simple Java class (**POJO**) that represents a table in the database.

It is mapped using the `@Entity` annotation.

```java
@Entity
public class Student {

    @Id
    private Long id;

    private String name;
}
```

### ✅ Characteristics

- Plain Old Java Object (POJO)
- Represents a Database Table
- Managed by Hibernate/JPA
- Stored inside the Database
- Contains business data

---

# 🌱 What is Entity Lifecycle?

## Definition

The **Entity Lifecycle** is the complete journey of an entity object from the moment it is created until it is removed from the database.

During this lifecycle, Hibernate changes the state of the object depending upon how it is managed.

---

# 🎯 Lifecycle States

There are **4 major states**.

```text
                new Student()
                      │
                      ▼
             🟡 Transient
                      │
               persist()
                      │
                      ▼
        🟢 Persistent (Managed)
          │               │
detach()  │               │ remove()
clear()   │               │
close()   ▼               ▼
      🔵 Detached      🔴 Removed
          │
          │ merge()
          ▼
    🟢 Persistent Again
```

---

# 🟡 1. Transient State

## Definition

A **Transient Entity** is a newly created Java object that exists **only inside JVM Heap Memory**.

Hibernate **does not know anything** about this object.

It is **not connected** to the Persistence Context.

---

## Characteristics

✅ Exists only in Java Memory

✅ Created using the `new` keyword

✅ Not managed by Hibernate

✅ No SQL Query is executed

✅ Not stored inside the Database

✅ Can be Garbage Collected

---

## Visualization

```text
Java Heap

┌───────────────────────────┐
│ Student Object            │
│ id = 1                    │
│ name = Rahul              │
└───────────────────────────┘

Hibernate
❌ Doesn't Know

Database
❌ No Record
```

---

## Example

```java
Student student = new Student();

student.setId(1);
student.setName("Rahul");
```

Current State

```
Transient
```

---

## How to Convert into Persistent?

```java
entityManager.persist(student);
```

OR

```java
session.persist(student);
```

---

# 🟢 2. Persistent (Managed) State

## Definition

A **Persistent Entity** is associated with the **Persistence Context**.

Hibernate starts managing the object.

Every change made to the object is automatically tracked.

---

## Characteristics

✅ Managed by Hibernate

✅ Stored inside Persistence Context

✅ Automatically synchronized with Database

✅ Dirty Checking Enabled

✅ SQL Generated Automatically

✅ Changes saved during Commit

---

## Visualization

```text
           Java Heap
      ┌─────────────────┐
      │ Student Object  │
      └─────────────────┘
              │
              │ Managed
              ▼
     Persistence Context
              │
              ▼
         Database Table
```

---

## Example

```java
Student student = new Student();

student.setName("Rahul");

entityManager.persist(student);
```

Current State

```
Persistent
```

---

### Modify Object

```java
student.setName("Amit");
```

You don't call

```java
update(student);
```

Hibernate automatically detects the change.

During Commit

```sql
UPDATE Student
SET name='Amit'
WHERE id=1;
```

---

# ⚡ Dirty Checking

## What is Dirty Checking?

Dirty Checking is Hibernate's feature that automatically detects changes in managed entities.

---

### How it Works

```text
Original Snapshot

Name = Rahul

↓

Current Object

Name = Amit

↓

Difference Found

↓

Generate UPDATE SQL
```

---

Example

```java
student.setName("Karan");
```

Hibernate automatically generates

```sql
UPDATE Student
SET name='Karan'
WHERE id=1;
```

No explicit SQL required.

---

# 🔵 3. Detached State

## Definition

A **Detached Entity** was once managed by Hibernate.

Later it becomes disconnected from the Persistence Context.

Hibernate stops tracking it.

---

## Characteristics

✅ Exists in Java Memory

❌ Hibernate doesn't manage it

❌ Dirty Checking Disabled

❌ Changes won't be saved automatically

---

## How Detached Happens

```java
entityManager.detach(student);
```

or

```java
entityManager.clear();
```

or

```java
entityManager.close();
```

---

## Visualization

```text
Java Heap

┌──────────────────────┐
│ Student              │
└──────────────────────┘

Hibernate

❌ No Tracking

Database

Existing Record
```

---

## Example

```java
Student student = entityManager.find(Student.class,1);

entityManager.detach(student);

student.setName("Rohit");
```

Will Database Update?

❌ NO

Because Hibernate is no longer tracking the object.

---

## Reattach Detached Object

```java
Student managed = entityManager.merge(student);
```

Now

```
Detached
      ↓
merge()
      ↓
Persistent
```

---

# 🔴 4. Removed State

## Definition

The entity is marked for deletion.

Hibernate schedules a DELETE query.

Actual deletion happens during

- flush()
- commit()

---

## Characteristics

✅ Managed until Commit

✅ Marked for Deletion

✅ Removed from Database

---

## Example

```java
Student student = entityManager.find(Student.class,1);

entityManager.remove(student);
```

Generated SQL

```sql
DELETE FROM Student
WHERE id=1;
```

---

# 🧠 Persistence Context

## Definition

Persistence Context is Hibernate's **First-Level Cache**.

It stores all Managed Entities.

Whenever Hibernate loads an entity, it keeps it inside the Persistence Context.

---

## Responsibilities

✅ Tracks Objects

✅ Dirty Checking

✅ Synchronizes Database

✅ Avoids Duplicate Queries

✅ Maintains Entity Lifecycle

---

## Visualization

```text
             Java Objects
                   │
                   ▼
        ┌────────────────────┐
        │ Persistence Context│
        └────────────────────┘
                   │
                   ▼
              Database
```

---

# 🔄 Lifecycle Transition Table

| Method | Current State | Next State |
|----------|--------------|------------|
| `new Student()` | None | Transient |
| `persist()` | Transient | Persistent |
| `find()` | Database | Persistent |
| `detach()` | Persistent | Detached |
| `clear()` | Persistent | Detached |
| `close()` | Persistent | Detached |
| `merge()` | Detached | Persistent |
| `remove()` | Persistent | Removed |
| `commit()` | Removed | Database Row Deleted |

---

# 💻 Complete Lifecycle Example

```java
Student student = new Student();          // Transient

entityManager.persist(student);           // Persistent

student.setName("Rahul");                 // Dirty Checking

entityManager.detach(student);            // Detached

student.setName("Amit");                  // Not Saved

Student managed = entityManager.merge(student);

entityManager.remove(managed);            // Removed

transaction.commit();                     // DELETE Executed
```

---

# 📊 State Comparison

| Feature | 🟡 Transient | 🟢 Persistent | 🔵 Detached | 🔴 Removed |
|----------|-------------|---------------|-------------|------------|
| Exists in Java | ✅ | ✅ | ✅ | ✅ |
| Exists in Database | ❌ | ✅ | ✅ | Pending Delete |
| Managed by Hibernate | ❌ | ✅ | ❌ | ✅ |
| Dirty Checking | ❌ | ✅ | ❌ | ❌ |
| Auto Update | ❌ | ✅ | ❌ | ❌ |

---

# 🎯 Interview Questions

### What is Entity Lifecycle?

> The sequence of different states through which an entity passes during its lifetime is called the Entity Lifecycle.

---

### How many states are there?

- Transient
- Persistent (Managed)
- Detached
- Removed

---

### Which state is managed by Hibernate?

✅ Persistent

---

### Which state exists only in JVM Memory?

✅ Transient

---

### Which state is not tracked by Hibernate?

✅ Detached

---

### Which state is scheduled for deletion?

✅ Removed

---

### Which method converts Transient → Persistent?

```java
persist()
```

---

### Which method converts Detached → Persistent?

```java
merge()
```

---

### Which method converts Persistent → Detached?

```java
detach()

clear()

close()
```

---

### Which method deletes an entity?

```java
remove()
```

---

# 📝 Quick Revision

```text
🟡 Transient
↓ persist()

🟢 Persistent
↓ detach()

🔵 Detached
↓ merge()

🟢 Persistent
↓ remove()

🔴 Removed
↓ commit()

🗑 Database Row Deleted
```

---

# 🧠 Memory Trick

| State | Remember As |
|--------|-------------|
| 🟡 **Transient** | Only Java knows the object |
| 🟢 **Persistent** | Hibernate manages the object |
| 🔵 **Detached** | Java knows it, Hibernate doesn't |
| 🔴 **Removed** | Hibernate will delete it during commit |

---

# ⭐ Key Takeaways

- **Transient** → Exists only in Java memory.
- **Persistent** → Managed by Hibernate and synchronized with the database.
- **Detached** → Exists in memory but is no longer tracked.
- **Removed** → Marked for deletion and removed during `commit()` or `flush()`.
- **Persistence Context** is the heart of Hibernate that manages entity states.
- **Dirty Checking** automatically detects changes in managed entities.
- `persist()` saves a new entity, `merge()` reattaches a detached entity, and `remove()` schedules deletion.

---

<div align="center">

## 🚀 One-Line Summary

> **Create (`new`) → Persist (`persist`) → Modify (Dirty Checking) → Detach (`detach`) → Merge (`merge`) → Remove (`remove`) → Commit (Deleted from Database)**

⭐ **Master these four states, and you've mastered the Entity Lifecycle in Hibernate!**

</div>