package com.example.locking;

import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Note 22 — simulate two users editing the same row concurrently.
 * Both load version 0. The first commit wins and bumps the version to 1; the second commit
 * still expects version 0, matches no row, and fails.
 */
public class OptimisticLockingDemo {

    public static void main(String[] args) {
        Long id;
        try (Session setup = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = setup.beginTransaction();
            Account account = new Account("Bob", 100);
            setup.persist(account);
            tx.commit();
            id = account.getId();
        }

        // Two independent sessions load the SAME row at version 0.
        try (Session sessionA = HibernateUtil.getSessionFactory().openSession();
             Session sessionB = HibernateUtil.getSessionFactory().openSession()) {

            Transaction txA = sessionA.beginTransaction();
            Account a = sessionA.find(Account.class, id);

            Transaction txB = sessionB.beginTransaction();
            Account b = sessionB.find(Account.class, id);

            // User A commits first -> version becomes 1.
            a.setBalance(a.getBalance() + 50);
            txA.commit();
            System.out.println("User A committed. Version is now " + a.getVersion());

            // User B still holds the stale version 0 -> conflict on commit.
            // Hibernate may surface this as jakarta.persistence.OptimisticLockException
            // or org.hibernate.StaleObjectStateException, so we catch broadly.
            try {
                b.setBalance(b.getBalance() - 30);
                txB.commit();
            } catch (RuntimeException ex) {
                System.out.println("User B was rejected (" + ex.getClass().getSimpleName()
                        + "): stale version, changes rolled back.");
            }
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
