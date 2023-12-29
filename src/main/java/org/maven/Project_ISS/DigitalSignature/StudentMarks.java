package org.maven.Project_ISS.DigitalSignature;

import org.maven.Project_ISS.socket.TCPClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class StudentMarks {
    Scanner scanner = new Scanner(System.in);
    String name;
    int mark ;
    StudentInfo student = new  StudentInfo(name,mark);

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

    public  List<StudentInfo> getStudentsWithMarks(List<StudentInfo> students) {
        List<StudentInfo> studentsWithMarks = new ArrayList<>();

        for (StudentInfo student : students) {
            studentsWithMarks.add(new StudentInfo(student.getName(), student.getMarks()));
            System.out.println("Name: " + student.getName() + ", Marks: " + student.getMarks());
        }

        return studentsWithMarks;
    }
    public byte[] getmessageByte(List<StudentInfo> studentList_){
        byte[] students_marks;
//        List<Student> students = new ArrayList<>();
        students_marks=getStudentsWithMarks(studentList_).toString().getBytes();
        System.out.println("getmessageByte"+ "\t"+Arrays.toString(students_marks));
        return  students_marks;
    }

    // A method that takes a list of student info objects and returns an array of bytes
    public byte[] convertListToBytes(List<StudentInfo> list) throws IOException {
        // Create a byte array output stream to write the bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // Create an object output stream to write the objects
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        // Loop through the list of student info objects
        for (StudentInfo si : list) {
            // Write the object to the output stream
            oos.writeObject(si);
        }
        // Close the output streams
        oos.close();
        baos.close();
        // Return the byte array
        return baos.toByteArray();
    }
    public List<StudentInfo> EnterMarks(){
        List<StudentInfo> studentList = new ArrayList<>();
        System.out.print("Enter the number of students: ");
        int numStudents = scanner.nextInt();
        for (int i = 0; i < numStudents; i++) {
            System.out.print("Enter the name of student " + (i + 1) + ": ");
            String studentName = scanner.next();
            System.out.print("Enter the marks for " + studentName + ": ");
            int marks = scanner.nextInt();
            studentList.add(new StudentInfo(studentName, marks));
            System.out.println("\nStudents with Marks:");
            List<StudentInfo> studentsWithMarks = getStudentsWithMarks(studentList);
//            for (StudentInfo student : studentsWithMarks) {
//                System.out.println("Name: " + student.getName() + ", Marks: " + student.getMarks());
//            }
        }
      return studentList;



    }


}

