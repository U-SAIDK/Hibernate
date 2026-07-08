package com.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The core entity reused by the basic examples (session, CRUD, transactions,
 * dirty checking, caching, querying, pagination).
 *
 * <p>Lombok generates the getters, setters, constructors and toString at compile time,
 * keeping the entity focused on its mapping. This is exactly how entities look in a
 * typical Spring Boot codebase.</p>
 */
@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor          // Hibernate requires a no-args constructor (it uses reflection).
@AllArgsConstructor
@ToString
public class Student {

    /**
     * IDENTITY = let PostgreSQL's auto-increment (SERIAL/IDENTITY column) assign the id.
     * We no longer set the id by hand — the database owns it.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    private String course;

    @Column(unique = true)
    private String email;

    /** Convenience constructor for the demos (id is generated, so it is omitted). */
    public Student(String name, String course, String email) {
        this.name = name;
        this.course = course;
        this.email = email;
    }
}
