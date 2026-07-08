package com.example.caching;

import com.example.model.Student;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Note 21 — First-Level Cache (a.k.a. the persistence context).
 *
 * <p>Every Session has its own first-level cache. Loading the same id twice in ONE session
 * hits the database only once — the second {@code find()} returns the cached instance.
 * The cache is per-session and cannot be turned off.</p>
 */
public class FirstLevelCacheDemo {

    public static void main(String[] args) {
        Long id;
        try (Session setup = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = setup.beginTransaction();
            Student s = new Student("Tim Berners-Lee", "Web", "tim@example.com");
            setup.persist(s);
            tx.commit();
            id = s.getId();
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            System.out.println(">> First find (expect a SELECT in the log):");
            Student a = session.find(Student.class, id);

            System.out.println(">> Second find in SAME session (expect NO SELECT):");
            Student b = session.find(Student.class, id);

            // Same session -> same cached object identity, only one DB round-trip.
            System.out.println("Same cached instance? " + (a == b));
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
