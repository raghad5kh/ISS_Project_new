package org.maven.Project_ISS.dao;

public interface ProfessorDao {
    void  save(Professor professor);
    void  update (Professor professor);
     boolean exist(int id_number);

     void updatePublicKey(String username,String publicKey);

    boolean exist_account(String username,String password);

    String get_national_number(int id_number);
    int get_id(String username);
}
