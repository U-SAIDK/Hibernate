package com.example;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// @Entity -> Marks this class as a Hibernate Entity.
// Means: This Java class will be mapped to a database table.
@Entity

// @Table -> Specifies the database table name.
@Table(name = "students")

public class Student {

// @Id Marks this field as Primary Key.
// Every table should have unique identifier.
    @Id
    private int id;

// Normal Column
    private String name;
// Normal Column
    private String course;

// Default Constructor :- Hibernate internally creates objects using reflection.
// Hibernate REQUIREMENT
    public Student() {
                     }
// Parameterized Constructor
public Student(int id, String name, String course) {
    this.id = id;
    this.name = name;
    this.course = course;
}

    // Getter for id
    public int getId() {
        return id;
    }

    // Setter for id
    public void setId(int id) {
        this.id = id;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Setter for name
    public void setName(String name) {
        this.name = name;
    }

    // Getter for course
    public String getCourse() {
        return course;
    }

    // Setter for course
    public void setCourse(String course) {
        this.course = course;
    }

// toString() -> HelpFull for Printing object (values instead of address)
@Override
public String toString() {
    return "Student{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", course='" + course + '\'' +
            '}';
                        }
}


