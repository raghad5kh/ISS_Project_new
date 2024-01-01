package org.maven.Project_ISS.dao;

import org.maven.Project_ISS.DigitalSignature.StudentInfo;

import java.util.List;

public interface ProfessorDao {
    void  save(Professor professor,int id_number);
    void  update (Professor professor);
     boolean exist(int id_number);

     void updatePublicKey(String username,String publicKey);
     String get_publicKey(String  username);

    boolean exist_account(String username,String password);

    String get_national_number(int id_number);
    int get_id(String username);
    String get_info(String username);
    void save_level4Data(String Server_response,String signature);
    int get_id_level4data(String signature);
    void save_list_students_marks(String name, int mark,int fk);
    String get_symbol(int number_year);
    String get_permission(String symbol);
    int get_numberYear(int id_number);
    List<StudentInfo> get_marks(String nameTable);
    String get_nameTable(String permission );



}
