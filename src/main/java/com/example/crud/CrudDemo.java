package com.example.crud;

import com.example.model.Student;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Note 09 — the four CRUD operations with the modern JPA-style Hibernate API.
 *
 * <pre>
 *   Create : session.persist(entity)
 *   Read   : session.find(Class, id)
 *   Update : mutate a managed entity  -> dirty checking writes the UPDATE
 *   Delete : session.remove(entity)
 * </pre>
 *
 * <p>Note we never call {@code save()}, {@code update()} or {@code delete()} — those are the
 * older, Hibernate-specific names. {@code persist/find/merge/remove} are the JPA methods you
 * will keep using in Spring Data JPA.</p>
 */
public class CrudDemo {

    public static void main(String[] args) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // ---------- CREATE ----------
            Transaction tx = session.beginTransaction();
            Student student = new Student("Grace Hopper", "Compilers", "grace@example.com");
            session.persist(student);
            tx.commit();
            Long id = student.getId();
            System.out.println("CREATE -> id " + id);

            // ---------- READ ----------
            tx = session.beginTransaction();
            Student loaded = session.find(Student.class, id);
            System.out.println("READ   -> " + loaded);
            tx.commit();

            // ---------- UPDATE (no update() call — dirty checking) ----------
            tx = session.beginTransaction();
            Student managed = session.find(Student.class, id);
            managed.setCourse("Distributed Systems");   // just mutate the managed object
            tx.commit();                                 // Hibernate flushes an UPDATE here
            System.out.println("UPDATE -> " + session.find(Student.class, id));

            // ---------- DELETE ----------
            tx = session.beginTransaction();
            Student toDelete = session.find(Student.class, id);
            session.remove(toDelete);
            tx.commit();
            System.out.println("DELETE -> now find returns: " + session.find(Student.class, id));

        } finally {
            HibernateUtil.shutdown();
        }
    }
}
