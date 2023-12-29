package org.maven.Project_ISS.DigitalSignature;

import org.maven.Project_ISS.socket.TCPClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class StudentMarks {
    Scanner scanner = new Scanner(System.in);
    String name;
    int mark ;
    Student student = new  Student(name,mark);

    public  void MakeCoiceToEntermarks(){
        System.out.println("Do you want to enter student marks"+"\n" +
                "1. Yes\n" +
                "2. No");
        int answer = scanner.nextInt();
        switch (answer){
            case 1 :
                EnterMarks();
                TCPClient.isEntered=true;
            break;
            case 2:
                break;
            default:

                System.out.print("THAT'S NOT ALLOWED! ");

        }
    }

    public  List<Student> getStudentsWithMarks(List<Student> students) {
        List<Student> studentsWithMarks = new ArrayList<>();

        for (Student student : students) {
            studentsWithMarks.add(new Student(student.getName(), student.getMarks()));
            System.out.println("Name: " + student.getName() + ", Marks: " + student.getMarks());
        }

        return studentsWithMarks;
    }
    public byte[] getmessageByte(List<Student> studentList_){
        byte[] students_marks;
//        List<Student> students = new ArrayList<>();
        students_marks=getStudentsWithMarks(studentList_).toString().getBytes();
        System.out.println("getmessageByte"+ "\t"+Arrays.toString(students_marks));
        return  students_marks;
    }
    public List<Student> EnterMarks(){
        List<Student> studentList = new ArrayList<>();
        System.out.print("Enter the number of students: ");
        int numStudents = scanner.nextInt();
        for (int i = 0; i < numStudents; i++) {
            System.out.print("Enter the name of student " + (i + 1) + ": ");
            String studentName = scanner.next();
            System.out.print("Enter the marks for " + studentName + ": ");
            int marks = scanner.nextInt();
            studentList.add(new Student(studentName, marks));
            List<Student> studentsWithMarks = getStudentsWithMarks(studentList);
            System.out.println("\nStudents with Marks:");
            for (Student student : studentsWithMarks) {
                System.out.println("Name: " + student.getName() + ", Marks: " + student.getMarks());
            }
        }
      return studentList;



    }


}

