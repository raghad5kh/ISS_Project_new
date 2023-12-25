package org.maven.Project_ISS.dao;

import java.util.List;

public interface StudentDao {

    void  save(Student student);

    void  update (Student student);
    boolean exist(int id_number);

    boolean exist_account(String username,String password);

    String get_national_number(int id_number);

    void updatePublicKey(String username,String publicKey);

    int get_id(String username);



}
