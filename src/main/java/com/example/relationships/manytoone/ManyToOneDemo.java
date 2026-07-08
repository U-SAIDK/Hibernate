package com.example.relationships.manytoone;

import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

/** Note 14 — many OrderEntry rows share one Customer. */
public class ManyToOneDemo {

    public static void main(String[] args) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Customer customer = new Customer("Acme Corp");
            session.persist(customer);

            session.persist(new OrderEntry("Keyboard", customer));
            session.persist(new OrderEntry("Monitor", customer));
            session.persist(new OrderEntry("Mouse", customer));

            tx.commit();

            OrderEntry order = session.find(OrderEntry.class, 1L);
            System.out.println("Order '" + order.getProduct()
                    + "' belongs to customer '" + order.getCustomer().getName() + "'");
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
