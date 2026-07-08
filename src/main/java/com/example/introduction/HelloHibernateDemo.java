package com.example.introduction;

import com.example.model.Student;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Note 01 / 02 — the smallest possible Hibernate program: save one object, no SQL written by hand.
 *
 * <p>Run this and watch the console: Hibernate generates the {@code INSERT} for you.
 * That single fact is the whole point of an ORM.</p>
 */
public class HelloHibernateDemo {

    public static void main(String[] args) {
        // 1. Get the shared, pre-built SessionFactory.
        // 2. Open a short-lived Session (a unit of work / a "conversation" with the DB).
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Transaction tx = session.beginTransaction();

            Student student = new Student("Ada Lovelace", "Computer Science", "ada@example.com");
            session.persist(student);   // No SQL written — Hibernate builds the INSERT.

            tx.commit();

            System.out.println("Saved student with generated id = " + student.getId());
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
