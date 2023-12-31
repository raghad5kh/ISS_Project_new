package org.maven.Project_ISS.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class StudentDaoImpl implements StudentDao {


    @Override
    public void save(Student student, int id_number) {
        Connection con = DBConnection.getConnection();
        if (con == null) {
            return;
        }
        int id = 0;
        String queryFind = "SELECT id FROM list_data WHERE id_number = ?;";
        try (PreparedStatement preparedStatement = con.prepareStatement(queryFind)) {
            preparedStatement.setInt(1, id_number);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    id = resultSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String query = "INSERT INTO students(username,password,address,phone_number,mobile_number,list_data_id) VALUES (?,?,?,?,?,?);";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setString(1, student.getUsername());
            preparedStatement.setString(2, student.getPassword());
            preparedStatement.setString(3, student.getAddress());
            preparedStatement.setInt(4, student.getPhone_number());
            preparedStatement.setInt(5, student.getMobile_number());
            preparedStatement.setInt(6, id);
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
    public void update(Student student) {
        Connection con = DBConnection.getConnection();
        if (con == null) {
            return;
        }
        String query = "UPDATE students SET username=?,password=?,address=?,phone_number=?,mobile_number=? WHERE id=?;";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setString(1, student.getUsername());
            preparedStatement.setString(2, student.getPassword());
            preparedStatement.setString(3, student.getAddress());
            preparedStatement.setInt(4, student.getPhone_number());
            preparedStatement.setInt(5, student.getMobile_number());
            preparedStatement.setInt(6, student.getId());
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
        if (con == null) {
            return false;
        }
        String query = "SELECT * FROM list_data WHERE id_number= ?;";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setInt(1, id_number);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next() && resultSet.getString("type").equals("s")) {
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
        if (con == null) {
            return false;
        }
        String query = "SELECT * FROM students WHERE username= ? AND password=?;";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
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

    public static int get_id_number(String username) throws SQLException {
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                return -1;
            }
            String query = "SELECT list_data_id FROM students WHERE username = ?;";
            int list_data_id = 0;
            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    list_data_id = resultSet.getInt("list_data_id");
                } else return -1;
                String query1 = "SELECT id_number FROM list_data WHERE id = ?;";
                PreparedStatement preparedStatement1 = con.prepareStatement(query1);
                preparedStatement1.setInt(1, list_data_id);
                ResultSet resultSet1 = preparedStatement1.executeQuery();
                if (resultSet1.next()) {
                    int id_number = resultSet1.getInt("id_number");
                    return id_number;
                } else return -1;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return -1;
        }
    }
    @Override

    public String get_national_number(int id_number) {
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                return null;
            }
            String query = "SELECT national_number FROM list_data WHERE id_number = ?;";
            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setInt(1, id_number);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String nationalNumber = resultSet.getString("national_number");
                        return nationalNumber;
                    } else return null;
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
    public void updatePublicKey(String username, String publicKey) {
        Connection connection = DBConnection.getConnection();
        if (connection == null) {
            return;
        }

        // SQL query to update the row
        String updateQuery = "UPDATE students SET publicKey = ? WHERE username = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            // Set parameters for the update query
            preparedStatement.setString(1, publicKey);
            preparedStatement.setString(2, username);

            // Execute the update query
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Row updated successfully.");
            } else {
                System.out.println("No rows updated. User ID not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isStudent(String username) throws SQLException {
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                return false;
            }
            String query = "SELECT list_data_id FROM students WHERE username = ?;";
            int list_data_id = 0;
            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    list_data_id = resultSet.getInt("list_data_id");
                } else return false;
                String query1 = "SELECT type FROM list_data WHERE id = ?;";
                PreparedStatement preparedStatement1 = con.prepareStatement(query1);
                preparedStatement1.setInt(1, list_data_id);
                ResultSet resultSet1 = preparedStatement1.executeQuery();
                if (resultSet1.next()) {
                    String type = resultSet1.getString("type");
                    return type.equals("s");
                } else return false;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }

    }

    @Override
    public int get_id(String username) {
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                return 0;
            }
            String query = "SELECT id FROM students WHERE username = ?;";
            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setString(1, username);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        return id;
                    } else return 0;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public  boolean exist_username(String username) {
        Connection con = DBConnection.getConnection();
        if (con == null) {
            return false;
        }
        String query = "SELECT * FROM students WHERE username= ?;";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next() ) {
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
}
