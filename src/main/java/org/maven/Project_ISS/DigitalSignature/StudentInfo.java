package org.maven.Project_ISS.DigitalSignature;

import java.io.Serializable;

public class StudentInfo implements Serializable {
    private static final long serialVersionUID=1L;
    private String name;
    private int marks;

    public StudentInfo(String name, int marks) {
        this.name = name;
        this.marks = marks;
    }

    public StudentInfo() {

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    public String getName() {
        return name;
    }

    public int getMarks() {
        return marks;
    }
}
