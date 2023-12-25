package org.maven.Project_ISS.socket.AuthForms;

import org.maven.Project_ISS.dao.Professor;
import org.maven.Project_ISS.dao.ProfessorDao;
import org.maven.Project_ISS.dao.Student;
import org.maven.Project_ISS.dao.StudentDao;

import java.io.PrintWriter;

public class SignInHandler {
    private final PrintWriter out;
    private final StudentDao studentDao;
    private final ProfessorDao professorDao;

    public SignInHandler(PrintWriter out, StudentDao studentDao, ProfessorDao professorDao) {
        this.out = out;
        this.studentDao = studentDao;
        this.professorDao = professorDao;
    }

    public void handleSignIn(int id_number ,String name,String password ) {
        boolean isStudentExist = studentDao.exist(id_number);
        boolean isProfExist = professorDao.exist(id_number);
        if (isStudentExist) {
            Student student = new Student(0,name,password,null,0,0);
            studentDao.save(student);
            System.out.println("Client: Sign in successful for " + name );
            out.println("Your SignIn has been done successfully. Welcome, " + name);
            System.out.println("SignIn Done!");
            out.println(id_number+","+name+","+password);
        }
        else if (isProfExist) {
            Professor professor = new Professor(0,name,password,null,0,0);
            professorDao.save(professor);
            System.out.println("Client: Sign in successful for " + name );
            out.println("Your SignIn has been done successfully. Welcome, " + name);
            System.out.println("SignIn Done!");
            out.println(id_number+","+name+","+password);

        }

       else  {
            System.out.println("Client: Failed to sign in with id_number: " + id_number +
                    ".This id_number doesn't exist in our records.");
            out.println("Sorry! This id_number {" + id_number + "} doesn't exist in our records");
        }
    }
}