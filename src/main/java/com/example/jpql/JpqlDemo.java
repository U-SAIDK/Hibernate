package com.example.jpql;

import com.example.model.Student;
import com.example.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

/**
 * Note 18 — JPQL is the JPA-standard query language. The Session implements the JPA
 * {@code EntityManager}, so the same query API is available.
 *
 * <p>This is the language behind Spring Data JPA's {@code @Query} annotation and behind
 * the SQL that derived query methods (e.g. {@code findByCourse}) generate.</p>
 */
public class JpqlDemo {

    public static void main(String[] args) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // Positional-style JPQL with LIKE.
            List<Student> results = session.createQuery(
                            "SELECT s FROM Student s WHERE s.name LIKE :pattern", Student.class)
                    .setParameter("pattern", "%a%")
                    .getResultList();

            System.out.println("Students whose name contains 'a':");
            results.forEach(System.out::println);
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
