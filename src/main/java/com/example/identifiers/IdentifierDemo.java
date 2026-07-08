package com.example.identifiers;

import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Note 11 — compare the three id strategies you actually use in modern apps.
 * Watch the console: each entity gets its id from a different mechanism.
 */
public class IdentifierDemo {

    public static void main(String[] args) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            IdentityEntity identity = new IdentityEntity("via IDENTITY column");
            SequenceEntity sequence = new SequenceEntity("via database SEQUENCE");
            UuidEntity uuid = new UuidEntity("via UUID");

            session.persist(identity);
            session.persist(sequence);
            session.persist(uuid);

            tx.commit();

            System.out.println("IDENTITY id : " + identity.getId());
            System.out.println("SEQUENCE id : " + sequence.getId());
            System.out.println("UUID id     : " + uuid.getId());
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
