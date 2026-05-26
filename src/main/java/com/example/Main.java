/// Hiberante Core Class

package com.example;

// Hibernate core classes
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.Scanner;

public class Main {
   public static void main(String[] args) {

/// STEP 1:-Create Configuration object
// Reads hibernate.cfg.xml file ; Loads all Hibernate configurations
       Configuration configuration = new Configuration();

// Loads configuration(hibernate.cfg.xml) file automatically using configure() method
       configuration.configure();


/// STEP 2 :- Build SessionFactory
// Heavyweight object -> Usually created once in entire application.
       SessionFactory factory = configuration.buildSessionFactory();


/// STEP 3 :- Open Session
// Session acts like temporary connection between Java application and database.
       Session session = factory.openSession();


/// STEP 4 :- Begin Transaction
//Transactions ensure: Atomicity - Either everything succeeds OR everything rolls back safely.
       Transaction tx = session.beginTransaction();

/// STEP 5:- Create Student object
       Student student = new Student();
       // Setting values
       Scanner scanner = new Scanner(System.in);

       System.out.printf("Enter your ID : ");
       int id = scanner.nextInt();

       scanner.nextLine(); // clears newline

       System.out.printf("Enter your Name : ");
       String name = scanner.next();

       System.out.printf("Enter your Course : ");
       String course = scanner.next();

       student.setId(id);
       student.setName(name);
       student.setCourse(course);

       // Displaying values
       System.out.println("ID: " + student.getId());
       System.out.println("Name: " + student.getName());
       System.out.println("Course: " + student.getCourse());

       scanner.close();

/// STEP 6:- Save object into database
// Hibernate Automatically Converts object -> SQL Query
       session.persist(student);

/// STEP 7:- Commit transaction
// Without commit:Data will NOT be permanently saved.
       tx.commit();

/// STEP 8:- Close session
       session.close();

/// STEP 9:- Close SessionFactory
       factory.close();

       System.out.println("Student object saved successfully!");
}
}


