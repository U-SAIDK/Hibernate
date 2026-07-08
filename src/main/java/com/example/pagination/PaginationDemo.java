package com.example.pagination;

import com.example.model.Student;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Note 19 — Pagination &amp; Sorting: never load a whole table into memory.
 *
 * <ul>
 *   <li>{@code setFirstResult(offset)}  -> SQL OFFSET</li>
 *   <li>{@code setMaxResults(size)}     -> SQL LIMIT</li>
 *   <li>{@code order by} in the query   -> SQL ORDER BY</li>
 * </ul>
 *
 * <p><b>Spring parallel:</b> this is exactly what a {@code Pageable} /
 * {@code PageRequest.of(page, size, Sort.by(...))} turns into under the hood.</p>
 */
public class PaginationDemo {

    private static final int PAGE_SIZE = 2;

    public static void main(String[] args) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            seed(session);

            for (int page = 0; page < 3; page++) {
                List<Student> pageContent = session.createQuery(
                                "from Student s order by s.name", Student.class)
                        .setFirstResult(page * PAGE_SIZE)   // OFFSET
                        .setMaxResults(PAGE_SIZE)           // LIMIT
                        .getResultList();

                System.out.println("Page " + page + " -> " + pageContent);
                if (pageContent.isEmpty()) break;
            }
        } finally {
            HibernateUtil.shutdown();
        }
    }

    private static void seed(Session session) {
        Transaction tx = session.beginTransaction();
        for (int i = 1; i <= 5; i++) {
            session.persist(new Student("Student-" + i, "Course-" + i, "s" + i + "@example.com"));
        }
        tx.commit();
    }
}
