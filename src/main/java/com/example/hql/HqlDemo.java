package com.example.hql;

import com.example.model.Student;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Note 18 — HQL / JPQL: you query ENTITIES and their fields, not tables and columns.
 *
 * <p>Note {@code from Student}, not {@code from students}, and {@code s.course}, not
 * the column name. Hibernate translates this object-oriented query into SQL.</p>
 *
 * <p>HQL and JPQL are effectively the same language here; JPQL is the standardized subset.
 * The exact same string works as a Spring Data JPA {@code @Query}.</p>
 */
public class HqlDemo {

    public static void main(String[] args) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            seed(session);

            // SELECT with a named parameter (never concatenate strings -> SQL injection).
            List<Student> byCourse = session.createQuery(
                            "from Student s where s.course = :course order by s.name", Student.class)
                    .setParameter("course", "Physics")
                    .getResultList();
            System.out.println("Physics students: " + byCourse);

            // Projection: select specific fields instead of whole entities.
            List<String> names = session.createQuery(
                            "select s.name from Student s order by s.name", String.class)
                    .getResultList();
            System.out.println("All names: " + names);

            // Aggregation.
            Long count = session.createQuery(
                            "select count(s) from Student s", Long.class)
                    .getSingleResult();
            System.out.println("Total students: " + count);
        } finally {
            HibernateUtil.shutdown();
        }
    }

    private static void seed(Session session) {
        Transaction tx = session.beginTransaction();
        session.persist(new Student("Marie Curie", "Physics", "marie@example.com"));
        session.persist(new Student("Richard Feynman", "Physics", "richard@example.com"));
        session.persist(new Student("Carl Sagan", "Astronomy", "carl@example.com"));
        tx.commit();
    }
}
