package com.example.lifecycle;

import com.example.model.Student;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Note 07 — the four entity states in action: Transient, Persistent, Detached, Removed.
 *
 * <p>Watch which mutations produce SQL and which are silently ignored.</p>
 */
public class EntityLifecycleDemo {

    public static void main(String[] args) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // --- TRANSIENT: a plain new object, Hibernate knows nothing about it ---
            Student student = new Student("Linus", "Operating Systems", "linus@example.com");
            System.out.println("TRANSIENT  -> id is null: " + (student.getId() == null));

            // --- PERSISTENT: persist() attaches it to the persistence context ---
            Transaction tx = session.beginTransaction();
            session.persist(student);
            System.out.println("PERSISTENT -> id assigned: " + student.getId());
            student.setCourse("Kernel Development");   // tracked by dirty checking
            tx.commit();                                // UPDATE flushed automatically

            // --- DETACHED: evict from the context; further changes are NOT tracked ---
            session.detach(student);
            student.setCourse("IGNORED - detached");   // no SQL will be produced
            System.out.println("DETACHED   -> change above will not be saved");

            // --- merge() brings a detached entity back under management ---
            tx = session.beginTransaction();
            Student managed = session.merge(student);
            tx.commit();

            // --- REMOVED: scheduled for deletion, gone after commit ---
            tx = session.beginTransaction();
            session.remove(managed);
            tx.commit();
            System.out.println("REMOVED    -> find now returns: "
                    + session.find(Student.class, managed.getId()));

        } finally {
            HibernateUtil.shutdown();
        }
    }
}
