package com.example.locking;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Note 22 — Optimistic Locking with {@code @Version}.
 * Hibernate adds {@code WHERE id=? AND version=?} to every UPDATE and increments the version.
 * If another transaction already changed the row, zero rows match and Hibernate throws
 * {@code OptimisticLockException} — no lost updates, no database-level locks held.
 */
@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String owner;

    private long balance;

    /** Managed entirely by Hibernate. Never set this by hand. */
    @Version
    private int version;

    public Account(String owner, long balance) {
        this.owner = owner;
        this.balance = balance;
    }
}
