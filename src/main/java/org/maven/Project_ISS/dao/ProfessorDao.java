package org.maven.Project_ISS.dao;

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

}
