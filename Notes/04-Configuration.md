# 04 · Configuration

## Introduction

Before Hibernate can do anything it needs to know: which database, what credentials, which dialect, and which entities exist. In this project that lives in **`src/main/resources/hibernate.cfg.xml`**.

## Why this concept exists

Hibernate must connect to *a* database and generate SQL in *that* database's flavor. Configuration is how you tell it. Externalizing it (in XML/properties) means you can change database or credentials without touching code.

## The configuration file

```xml
<hibernate-configuration>
  <session-factory>
    <!-- connection -->
    <property name="hibernate.connection.driver_class">org.postgresql.Driver</property>
    <property name="hibernate.connection.url">jdbc:postgresql://localhost:5432/hibernate_demo</property>
    <property name="hibernate.connection.username">postgres</property>
    <property name="hibernate.connection.password">********</property>

    <!-- behaviour -->
    <property name="hibernate.dialect">org.hibernate.dialect.PostgreSQLDialect</property>
    <property name="hibernate.show_sql">true</property>
    <property name="hibernate.format_sql">true</property>
    <property name="hibernate.hbm2ddl.auto">update</property>

    <!-- entities -->
    <mapping class="com.example.model.Student"/>
  </session-factory>
</hibernate-configuration>
```

## Key properties explained

| Property | Purpose |
|----------|---------|
| `connection.url / username / password` | How to reach the database |
| `dialect` | Which SQL flavor to generate (auto-detected in Hibernate 6, but explicit is clear) |
| `show_sql` / `format_sql` | Print (and pretty-print) generated SQL — great for learning |
| `hbm2ddl.auto` | Schema strategy (see below) |
| `<mapping class=.../>` | Registers an entity |

## `hbm2ddl.auto` values

| Value | Meaning | Use when |
|-------|---------|----------|
| `none` | Do nothing | Production (schema managed elsewhere) |
| `validate` | Check schema matches entities, change nothing | Production |
| `update` | Add missing tables/columns, never drop | Learning / dev |
| `create` | Drop + recreate schema on startup | Throwaway tests |
| `create-drop` | Like create, also drop on shutdown | Unit tests |

> ⚠️ Never use `create`, `create-drop`, or even `update` against a production database — you can lose data or drift silently. Real projects use **Flyway** or **Liquibase** migrations plus `validate`.

## Best practices

- Keep secrets (passwords) out of source control — use environment variables in real projects.
- `validate` + a migration tool in production; `update` only for local learning.
- Turn `show_sql` off in production (noise + minor overhead).

## Common mistakes

- Shipping `hbm2ddl.auto=update` to production.
- Wrong dialect → subtly wrong SQL.
- Forgetting to register an entity (`<mapping class=...>`), causing "Unknown entity" errors.

## Summary

Configuration tells Hibernate where the database is, how to speak its SQL, how to handle the schema, and which entities to manage.

## How this appears in Spring Data JPA

The XML disappears. Everything above becomes a few lines in **`application.properties`** (or `.yml`):

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/hibernate_demo
spring.datasource.username=postgres
spring.datasource.password=********
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
```

Spring Boot also **auto-scans** for `@Entity` classes, so you no longer list each one. Same properties, same Hibernate — just relocated and auto-discovered.

---
➡️ **Next:** [05 · SessionFactory and Session](05-SessionFactory-and-Session.md)
