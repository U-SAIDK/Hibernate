<div align="center">

# 🎓 Hibernate → Spring Data JPA — A Structured Learning Course

### *Learn modern Hibernate the right way, so Spring Data JPA feels obvious.*

![Java](https://img.shields.io/badge/Java-17-red?style=for-the-badge&logo=openjdk)
![Hibernate](https://img.shields.io/badge/Hibernate-6.6-59666C?style=for-the-badge&logo=hibernate)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-42.7-4169E1?style=for-the-badge&logo=postgresql)
![Maven](https://img.shields.io/badge/Maven-3.8+-C71A36?style=for-the-badge&logo=apachemaven)

</div>

---

## 📖 Project Overview

This repository is a **self-paced course**, not a random pile of examples. It teaches exactly the subset of **modern Hibernate** that every Spring Boot / Spring Data JPA developer needs — no legacy features, no encyclopedia.

It is built as **25 sequential notes** in [`Notes/`](Notes/), each paired with a **small runnable Java example**. Every note ends with a **"How this appears in Spring Data JPA"** section, so you're constantly building the bridge to your next step.

Start at Note 01, read down, run the matching demo, repeat. By Note 25 you'll be ready for Spring Data JPA.

---

## 🎯 Learning Objectives

By the end of this course you will be able to:

- Explain what an ORM is and why Hibernate exists.
- Configure Hibernate and understand `SessionFactory` vs `Session`.
- Map entities and understand the **entity lifecycle** and **persistence context**.
- Perform CRUD inside transactions using the modern JPA API.
- Choose the right **primary-key generation** strategy.
- Model **all four relationships** and control **cascade** and **fetch** behavior.
- Query with **JPQL/HQL**, paginate, and sort.
- Understand **dirty checking**, the **first-level cache**, and **optimistic locking**.
- Avoid the classic performance traps (**N+1**, `LazyInitializationException`).
- Map every concept onto how **Spring Data JPA** uses it internally.

---

## 🗂️ Repository Structure

```
Hibernate/
├── README.md                     ← you are here
├── pom.xml                       ← Maven build (Java 17, Hibernate 6.6)
│
├── Notes/                        ← the 25-part course (read in order)
│   ├── 01-Introduction-to-Hibernate.md
│   ├── 02-ORM-and-Why-Hibernate.md
│   ├── ...
│   └── 25-Hibernate-to-Spring-Data-JPA.md
│
└── src/main/
    ├── resources/
    │   └── hibernate.cfg.xml      ← DB config + entity registration
    └── java/com/example/
        ├── util/HibernateUtil.java        ← single shared SessionFactory
        ├── model/Student.java             ← core entity reused by basics
        ├── introduction/                  ← Notes 01–02
        ├── session/                       ← Notes 03, 05
        ├── crud/                          ← Note 09
        ├── transactions/                  ← Note 10
        ├── lifecycle/                     ← Note 07
        ├── identifiers/                   ← Note 11
        ├── relationships/
        │   ├── onetoone/                  ← Note 12
        │   ├── onetomany/                 ← Note 13
        │   ├── manytoone/                 ← Note 14
        │   └── manytomany/                ← Note 15
        ├── hql/                           ← Note 18
        ├── jpql/                          ← Note 18
        ├── pagination/                    ← Note 19
        ├── dirtychecking/                 ← Note 20
        ├── caching/                       ← Note 21
        └── locking/                       ← Note 22
```

Each package contains a `...Demo` class with a `main()` method — small, focused, and runnable on its own.

---

## ✅ Prerequisites

- **Java 17** (JDK) — `java -version`
- **Maven 3.8+** — `mvn -version`
- **PostgreSQL** running locally with a database named `hibernate_demo`
- Basic Java (classes, objects, collections) and a little SQL

Create the database:

```sql
CREATE DATABASE hibernate_demo;
```

Then set your credentials in [`src/main/resources/hibernate.cfg.xml`](src/main/resources/hibernate.cfg.xml)
(`hibernate.connection.username` / `password`).

> 🔐 The committed config uses a local development password. For anything real, move credentials to environment variables and never commit them.

---

## 🛠️ Technologies Used

| Technology | Version | Role |
|------------|---------|------|
| Java | 17 (LTS) | Language (Spring Boot 3 baseline) |
| Hibernate ORM | 6.6.x | JPA implementation |
| Jakarta Persistence | 3.1 | The JPA API (`jakarta.persistence.*`) |
| PostgreSQL JDBC | 42.7.x | Database driver |
| Lombok | 1.18.x | Removes entity boilerplate |
| Maven | 3.8+ | Build & dependencies |

---

## ▶️ How to Run Examples

Compile everything:

```bash
mvn clean compile
```

Run any demo by its fully-qualified class name, e.g.:

```bash
# The very first "hello Hibernate" insert
mvn exec:java -Dexec.mainClass=com.example.introduction.HelloHibernateDemo

# CRUD
mvn exec:java -Dexec.mainClass=com.example.crud.CrudDemo

# One-to-many relationship
mvn exec:java -Dexec.mainClass=com.example.relationships.onetomany.OneToManyDemo

# Optimistic locking
mvn exec:java -Dexec.mainClass=com.example.locking.OptimisticLockingDemo
```

> 💡 With `hibernate.show_sql` / `format_sql` enabled, every demo prints the SQL Hibernate generates — read it. That's where the learning happens.
>
> ℹ️ If `exec:java` isn't configured in your Maven setup, run the demo from your IDE instead (right-click the `...Demo` class → Run), or add the `exec-maven-plugin`.

---

## 🧭 Learning Roadmap

```
 FOUNDATIONS            MAPPING & DATA           RELATIONSHIPS
 ┌───────────────┐      ┌───────────────┐        ┌───────────────┐
 │ 01 Intro      │      │ 06 Entities   │        │ 12 One-to-One │
 │ 02 ORM        │ ───► │ 07 Lifecycle  │ ────►  │ 13 One-to-Many│
 │ 03 Architecture│     │ 08 Persistence│        │ 14 Many-to-One│
 │ 04 Config     │      │ 09 CRUD       │        │ 15 Many-to-Many│
 │ 05 Factory/Sess│     │ 10 Transactions│       │ 16 Cascade    │
 └───────────────┘      │ 11 Id Gen     │        │ 17 Fetch Types│
                        └───────────────┘        └───────────────┘
                                                         │
 THE BRIDGE                 GOING PRO                    ▼
 ┌───────────────┐      ┌───────────────┐        ┌───────────────┐
 │ 25 → Spring   │ ◄─── │ 23 Performance│ ◄───── │ 18 HQL/JPQL   │
 │    Data JPA   │      │ 24 Mistakes   │        │ 19 Pagination │
 └───────────────┘      │ 22 Locking    │        │ 20 Dirty Check│
                        │ 21 1st Cache  │        └───────────────┘
                        └───────────────┘
```

---

## 📚 Recommended Study Order

Read the notes **in numerical order** — each builds on the last. For every note:

1. **Read** the note in [`Notes/`](Notes/).
2. **Run** the referenced `...Demo` class.
3. **Read the SQL** it prints and match it to the concept.
4. **Re-read** the *"How this appears in Spring Data JPA"* section.

| # | Note | Runnable example |
|---|------|------------------|
| 01 | Introduction to Hibernate | `introduction.HelloHibernateDemo` |
| 02 | ORM and Why Hibernate | `introduction.HelloHibernateDemo` |
| 03 | Hibernate Architecture | `session.SessionDemo` |
| 04 | Configuration | `hibernate.cfg.xml` |
| 05 | SessionFactory and Session | `session.SessionDemo` |
| 06 | Entities | `model.Student` |
| 07 | Entity Lifecycle | `lifecycle.EntityLifecycleDemo` |
| 08 | Persistence Context | `caching.FirstLevelCacheDemo` |
| 09 | CRUD Operations | `crud.CrudDemo` |
| 10 | Transactions | `transactions.TransactionDemo` |
| 11 | Primary Key Generation | `identifiers.IdentifierDemo` |
| 12 | One-to-One | `relationships.onetoone.OneToOneDemo` |
| 13 | One-to-Many | `relationships.onetomany.OneToManyDemo` |
| 14 | Many-to-One | `relationships.manytoone.ManyToOneDemo` |
| 15 | Many-to-Many | `relationships.manytomany.ManyToManyDemo` |
| 16 | Cascade Types | `relationships.onetomany.OneToManyDemo` |
| 17 | Fetch Types | `relationships.onetomany.Employee` |
| 18 | HQL and JPQL | `hql.HqlDemo`, `jpql.JpqlDemo` |
| 19 | Pagination and Sorting | `pagination.PaginationDemo` |
| 20 | Dirty Checking | `dirtychecking.DirtyCheckingDemo` |
| 21 | First-Level Cache | `caching.FirstLevelCacheDemo` |
| 22 | Optimistic Locking | `locking.OptimisticLockingDemo` |
| 23 | Performance Tips | *(concepts)* |
| 24 | Common Mistakes | *(concepts)* |
| 25 | Hibernate → Spring Data JPA | *(the bridge)* |

---

## 🎁 What You'll Learn Before Spring Data JPA

By finishing this course you will understand **what Spring Data JPA is doing for you**, not just how to call it:

- Why `save()` is sometimes `persist` and sometimes `merge`.
- Why a field change in a `@Transactional` method saves **without** calling `save()` (dirty checking).
- Where `LazyInitializationException` comes from — and how to fix it properly.
- Why your list endpoint fired 200 queries (**N+1**) — and how to make it 1.
- What `@Version`, `@EntityGraph`, `Pageable`, and `ddl-auto` actually do underneath.

Spring Data JPA stops being magic and becomes **"the same thing, less code."**

---

## 🚀 Next Step: Spring Data JPA

You're ready. Head to **[Note 25](Notes/25-Hibernate-to-Spring-Data-JPA.md)** for the concrete migration path:

1. Generate a Spring Boot project (Spring Web + Spring Data JPA + PostgreSQL).
2. Move the `Student` entity across **unchanged**.
3. Replace `CrudDemo` with a one-line `StudentRepository extends JpaRepository<Student, Long>`.
4. Delete `HibernateUtil` + `hibernate.cfg.xml`; let `application.properties` and Spring Boot take over.
5. Add `@Transactional` services and watch every concept from this course still running underneath.

<div align="center">

**Start now → [Note 01 · Introduction to Hibernate](Notes/01-Introduction-to-Hibernate.md)**

</div>
