package com.example.transactions;

import com.example.model.Student;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Note 10 — transactions guarantee atomicity: all-or-nothing.
 *
 * <p>This demo deliberately throws after the first insert to show that a rollback
 * undoes everything done since {@code beginTransaction()}.</p>
 *
 * <p><b>Spring parallel:</b> you almost never write begin/commit/rollback in Spring.
 * You annotate a method with {@code @Transactional} and Spring runs exactly this
 * try/commit/catch-rollback pattern around it.</p>
 */
public class TransactionDemo {

    public static void main(String[] args) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                session.persist(new Student("Alan Turing", "Logic", "alan@example.com"));

                // Simulate a failure half-way through the unit of work.
                if (true) {
                    throw new IllegalStateException("Something went wrong!");
                }

                session.persist(new Student("Never Saved", "N/A", "never@example.com"));
                tx.commit();
            } catch (RuntimeException ex) {
                tx.rollback();   // undo the first insert too — nothing is persisted
                System.out.println("Rolled back because: " + ex.getMessage());
            }
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
