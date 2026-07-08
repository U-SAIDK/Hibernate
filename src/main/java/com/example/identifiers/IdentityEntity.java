package com.example.identifiers;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Note 11 — GenerationType.IDENTITY.
 * The database column auto-increments (PostgreSQL IDENTITY/SERIAL). Hibernate must INSERT
 * first to learn the id, so it cannot batch inserts of this entity.
 */
@Entity
@Table(name = "id_identity")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class IdentityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String label;

    public IdentityEntity(String label) {
        this.label = label;
    }
}
