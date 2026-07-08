package com.example.session;

import com.example.model.Student;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Note 05 — SessionFactory vs Session.
 *
 * <ul>
 *   <li><b>SessionFactory</b>: heavyweight, thread-safe, built ONCE. Analogous to a
 *       connection-pool + mapping metadata. Shared across the whole app.</li>
 *   <li><b>Session</b>: lightweight, NOT thread-safe, short-lived. One per unit of work.
 *       Wraps a JDBC connection and the first-level cache (persistence context).</li>
 * </ul>
 *
 * <p><b>Spring parallel:</b> SessionFactory ≈ {@code EntityManagerFactory} (one bean),
 * Session ≈ {@code EntityManager} (one per transaction/request).</p>
 */
public class SessionDemo {

    public static void main(String[] args) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        System.out.println("SessionFactory is the same instance every time: " + factory);

        // Each openSession() is a brand-new, independent conversation with the database.
        try (Session first = factory.openSession();
             Session second = factory.openSession()) {

            System.out.println("Two distinct sessions: " + (first != second));

            // A read inside a session. find() returns null if the row does not exist.
            Student s = first.find(Student.class, 1L);
            System.out.println("Student #1 = " + s);
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
