package org.maven.Project_ISS.DigitalSignature;

import java.io.Serializable;

public class Student implements Serializable {
    private static final long serialVersionUID=1L;
    private String name;
    private int marks;

    public Student(String name, int marks) {
        this.name = name;
        this.marks = marks;
    }

    public Student() {

    }

    public String getName() {
        return name;
    }

    public int getMarks() {
        return marks;
    }
}
