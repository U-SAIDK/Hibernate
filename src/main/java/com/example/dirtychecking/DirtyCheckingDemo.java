package com.example.dirtychecking;

import com.example.model.Student;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Note 20 — Dirty Checking: Hibernate auto-detects changes to managed entities and
 * writes the UPDATE for you. There is NO update() call anywhere below.
 *
 * <p>On flush/commit, Hibernate compares each managed entity to the snapshot it took
 * when the entity was loaded. Any changed field triggers an UPDATE.</p>
 */
public class DirtyCheckingDemo {

    public static void main(String[] args) {
        Long id;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Student s = new Student("Margaret Hamilton", "Software Engineering", "maggie@example.com");
            session.persist(s);
            tx.commit();
            id = s.getId();
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Student managed = session.find(Student.class, id);  // now PERSISTENT + snapshot taken
            managed.setCourse("Fault-Tolerant Systems");        // just a setter call

            // We never call update(). On commit, dirty checking emits:
            //   UPDATE students SET course=? WHERE id=?
            tx.commit();

            System.out.println("Persisted change via dirty checking: "
                    + session.find(Student.class, id).getCourse());
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
