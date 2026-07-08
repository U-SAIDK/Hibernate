package com.example.relationships.onetoone;

import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

/** Note 12 — persist a Person and let cascade save its Passport in one call. */
public class OneToOneDemo {

    public static void main(String[] args) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Person person = new Person("Nikola Tesla");
            person.setPassport(new Passport("P-1234567"));

            session.persist(person);   // cascade ALL also inserts the Passport
            tx.commit();

            Person loaded = session.find(Person.class, person.getId());
            System.out.println(loaded.getName() + " has passport "
                    + loaded.getPassport().getPassportNumber());
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
