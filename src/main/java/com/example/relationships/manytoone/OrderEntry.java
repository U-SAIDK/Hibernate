package com.example.relationships.manytoone;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Note 14 — Many-to-One (owning side). Many orders belong to one customer.
 * The FK column {@code customer_id} lives here. LAZY fetch avoids loading the customer
 * unless you actually touch {@code getCustomer()}.
 */
@Entity
@Table(name = "order_entries")
@Getter
@Setter
@NoArgsConstructor
public class OrderEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public OrderEntry(String product, Customer customer) {
        this.product = product;
        this.customer = customer;
    }
}
