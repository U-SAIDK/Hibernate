package com.example.identifiers;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Note 11 — GenerationType.SEQUENCE (the recommended default on PostgreSQL).
 * Hibernate asks a database sequence for the next id, which allows JDBC batch inserts.
 * {@code allocationSize} lets Hibernate reserve a block of ids to reduce round-trips.
 */
@Entity
@Table(name = "id_sequence")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class SequenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen")
    @SequenceGenerator(name = "seq_gen", sequenceName = "demo_seq", allocationSize = 50)
    private Long id;

    private String label;

    public SequenceEntity(String label) {
        this.label = label;
    }
}
