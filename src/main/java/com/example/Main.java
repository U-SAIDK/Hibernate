///  ORM (OBject-Relational Mapping) is a Technique used to Map Java Objects to Database Tables
// It Allows developers to work with db using oop concepts instead of Raw Queries
//        | Java     | Database |
//        | -------- | -------- |
//        | Class    | Table    |
//        | Object   | Row      |
//        | Variable | Column   |
// Java Object -> ORM Framework (Hibernate) -> SQL Generated Automatically -> JDBC -> DB
// Hibernate is a Popular java ORM Framework.

/// JPA :- Java Persistence Api (Persistence -> Permanently save)
// JPA Provides certain Rules & it is a Way to Achieve ORM ,TO use JPA persistence Provider is Needed
// Persistence Provider is a specific implementation of JPA ; Ex:- Hibernate , Open JPA
// These providers implement JPA interfaces & provide underlying Functionality to interact with DB



package com.example;

// Hibernate core classes
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.Scanner;

/// Hiberante Core Class
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

/// STEP 5:- Create Student object (CRUD)
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

// =================== CREATE(inserting Values) Using Setter ===================
       student.setId(id);
       student.setName(name);
       student.setCourse(course);

// =================== READ (Displaying values) using getter ===================
       System.out.println("ID: " + student.getId());
       System.out.println("Name: " + student.getName());
       System.out.println("Course: " + student.getCourse());

       scanner.close();

/// STEP 6:- Save object into database (Save = INSERT)
// Hibernate Automatically Converts object -> SQL Query
       session.persist(student);

/// STEP 7:- Commit transaction
// Without commit:Data will NOT be permanently saved.
       tx.commit();



// =================== UPDATE OPERATION ===================
// session.get() ->Fetches object from database using Primary Key.
// Syntax:- session.get(ClassName.class, primaryKey)
       tx = session.beginTransaction(); // New transaction required for UPDATE
       Student updateStudent = session.get(Student.class, 2);
       if(updateStudent != null){

           updateStudent.setName("UpdatedName");
           updateStudent.setCourse("Updated Course");

// { Optional } merge() -> Updates existing object in database.(Hibernate generates UPDATE SQL query.)
// Hibernate Auto Detects Changes with Concept called Dirty Checking
       session.merge(updateStudent);
       System.out.println("Student & Courses Updated");
       }else{
           System.out.println("Student NOT FOUND!");
       }
       tx.commit(); // Commit UPDATE changes

// READ THE UPDATED DATA
       Student updatedData = session.get(Student.class, id);
       System.out.println("\nUpdated Student Data:");
       System.out.println(updatedData);


// ========== DELETE OPERATION ====================

// New Transaction Required for DELETE
       tx = session.beginTransaction();
// Taking ID Input from User for Deletion
       System.out.print("\nEnter Student ID to DELETE : ");
       int deleteId = scanner.nextInt();


// session.get() -> Fetches Object from Database using Primary Key
       Student deleteStudent = session.get(Student.class, deleteId);

// Checking if Student Exists
       if (deleteStudent != null) {

// remove() -> Deletes Object from Database
           session.remove(deleteStudent);
           System.out.println("\nStudent Deleted Successfully!");
       } else {System.out.println("\nStudent NOT FOUND!");}

        tx.commit(); // Commit DELETE Changes




/// STEP 8:- Close session
       session.close();

/// STEP 9:- Close SessionFactory
       factory.close();
       System.out.println("Student object saved successfully!");
}
}


