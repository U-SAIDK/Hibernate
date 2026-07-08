package com.example.relationships.manytomany;

import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

/** Note 15 — authors and books linked through a join table. */
public class ManyToManyDemo {

    public static void main(String[] args) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Author gof1 = new Author("Erich Gamma");
            Author gof2 = new Author("Ralph Johnson");
            Book patterns = new Book("Design Patterns");

            gof1.addBook(patterns);
            gof2.addBook(patterns);   // same book, two authors

            session.persist(gof1);
            session.persist(gof2);
            tx.commit();

            Book loaded = session.find(Book.class, patterns.getId());
            System.out.println("'" + loaded.getTitle() + "' written by:");
            loaded.getAuthors().forEach(a -> System.out.println("  - " + a.getName()));
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
