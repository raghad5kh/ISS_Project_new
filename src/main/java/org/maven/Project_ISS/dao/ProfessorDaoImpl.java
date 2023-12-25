package org.maven.Project_ISS.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfessorDaoImpl implements ProfessorDao{
    @Override
    public void save(Professor professor) {
        Connection con = DBConnection.getConnection();
        if(con==null){
            return ;
        }
        String query ="INSERT INTO professor(username,password,address,phone_number,mobile_number) VALUES (?,?,?,?,?);";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)){
            preparedStatement.setString(1,professor.getUsername());
            preparedStatement.setString(2,professor.getPassword());
            preparedStatement.setString(3,professor.getAddress());
            preparedStatement.setInt(4,professor.getPhone_number());
            preparedStatement.setInt(5,professor.getMobile_number());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void update(Professor professor) {
        Connection con = DBConnection.getConnection();
        if(con==null){
            return ;
        }
        String query ="UPDATE professor SET username=?,password=?,address=?,phone_number=?,mobile_number=? WHERE id=?;";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)){
            preparedStatement.setString(1,professor.getUsername());
            preparedStatement.setString(2,professor.getPassword());
            preparedStatement.setString(3,professor.getAddress());
            preparedStatement.setInt(4,professor.getPhone_number());
            preparedStatement.setInt(5,professor.getMobile_number());
            preparedStatement.setInt(6,professor.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean exist(int id_number) {
        Connection con = DBConnection.getConnection();
        if(con==null){
            return false;
        }
        String query ="SELECT * FROM list_pros WHERE id_number= ?;";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)){
            preparedStatement.setInt(1,id_number);

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean exist_account(String username, String password) {
        Connection con = DBConnection.getConnection();
        if(con==null){
            return false;
        }
        String query ="SELECT * FROM professor WHERE username= ? AND password=?;";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)){
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public String get_national_number(int id_number) {
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                return null;
            }
            String query = "SELECT national_number FROM list_pros WHERE id_number = ?;";
            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setInt(1, id_number);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String nationalNumber = resultSet.getString("national_number");
                        return nationalNumber;
                    }
                    else return null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int get_id(String username) {
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                return 0;
            }
            String query = "SELECT id FROM professor WHERE username = ?;";
            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setString(1, username);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        return id;
                    }
                    else return 0;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    }

