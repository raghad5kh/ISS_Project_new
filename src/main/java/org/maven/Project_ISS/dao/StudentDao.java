package org.maven.Project_ISS.dao;

import org.maven.Project_ISS.DigitalSignature.StudentInfo;

import java.util.List;

public interface StudentDao {

    void  save(Student student,int id_number);

    void  update (Student student);
    boolean exist(int id_number);

    boolean exist_account(String username);

    String get_national_number(int id_number);

    void updatePublicKey(String username,String publicKey);

    int get_id(String username);
    String get_info(String username);
    String get_publicKey(String  username);
    int get_numberYear(int id_number);
    String get_symbol(int number_year);
    List<StudentInfo> get_marks(String nameTable);
    String get_nameTable(String permission );
    String get_password(String username);



}
