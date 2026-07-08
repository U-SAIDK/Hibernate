package com.example.relationships.manytoone;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Note 14 — the "one" side of a unidirectional Many-to-One.
 * Customer knows nothing about its orders; only OrderEntry points back to Customer. This is
 * the leanest, most common relationship mapping in real applications.
 */
@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Customer(String name) {
        this.name = name;
    }
}
