package com.example.relationships.onetomany;

import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

/** Note 13 — save one Department and cascade-save its Employees. */
public class OneToManyDemo {

    public static void main(String[] args) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Department engineering = new Department("Engineering");
            engineering.addEmployee(new Employee("Dennis Ritchie"));
            engineering.addEmployee(new Employee("Ken Thompson"));

            session.persist(engineering);   // cascade ALL inserts both employees
            tx.commit();

            Department loaded = session.find(Department.class, engineering.getId());
            System.out.println(loaded.getName() + " has "
                    + loaded.getEmployees().size() + " employees:");
            loaded.getEmployees().forEach(e -> System.out.println("  - " + e.getName()));
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
