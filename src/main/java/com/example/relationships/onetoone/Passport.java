package com.example.relationships.onetoone;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Note 12 — One-to-One (inverse side).
 * {@code mappedBy} says "the Person.passport field owns this relationship"; no extra FK column
 * is created on this table.
 */
@Entity
@Table(name = "passports")
@Getter
@Setter
@NoArgsConstructor
public class Passport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String passportNumber;

    @OneToOne(mappedBy = "passport")
    private Person person;

    public Passport(String passportNumber) {
        this.passportNumber = passportNumber;
    }
}
