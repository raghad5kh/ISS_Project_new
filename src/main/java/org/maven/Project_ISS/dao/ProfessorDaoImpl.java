package org.maven.Project_ISS.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfessorDaoImpl implements ProfessorDao {
    @Override
    public void save(Professor professor, int id_number) {
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


        String query = "INSERT INTO professor(username,password,address,phone_number,mobile_number,list_data_id) VALUES (?,?,?,?,?,?);";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setString(1, professor.getUsername());
            preparedStatement.setString(2, professor.getPassword());
            preparedStatement.setString(3, professor.getAddress());
            preparedStatement.setInt(4, professor.getPhone_number());
            preparedStatement.setInt(5, professor.getMobile_number());
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
    public void update(Professor professor) {
        Connection con = DBConnection.getConnection();
        if (con == null) {
            return;
        }
        String query = "UPDATE professor SET username=?,password=?,address=?,phone_number=?,mobile_number=? WHERE id=?;";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setString(1, professor.getUsername());
            preparedStatement.setString(2, professor.getPassword());
            preparedStatement.setString(3, professor.getAddress());
            preparedStatement.setInt(4, professor.getPhone_number());
            preparedStatement.setInt(5, professor.getMobile_number());
            preparedStatement.setInt(6, professor.getId());
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
            if (resultSet.next() && resultSet.getString("type").equals("p")) {
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
        String query = "SELECT * FROM professor WHERE username= ? AND password=?;";
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

    @Override
    public void updatePublicKey(String username, String publicKey) {
        Connection connection = DBConnection.getConnection();
        if (connection == null) {
            return;
        }

        // SQL query to update the row
        String updateQuery = "UPDATE professor SET publicKey = ? WHERE username = ?";

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

    @Override
    public String get_publicKey(String  username) {
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                return null;
            }
            String query = "SELECT publicKey FROM professor WHERE username = ?;";
            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setString(1, username);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String publicKey = resultSet.getString("publicKey");
                        return publicKey;
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

    public static int get_id_number(String username) throws SQLException {
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                return -1;
            }
            String query = "SELECT list_data_id FROM professor WHERE username = ?;";
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
    public String get_info(String username) {
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                return null;
            }

            String query = "SELECT address, phone_number, mobile_number FROM professor WHERE username = ?;";
            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setString(1, username);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {

                        String address = resultSet.getString("address");
                        String phoneNumber = resultSet.getString("phone_number");
                        String mobileNumber = resultSet.getString("mobile_number");

                        return "Address: " + address + ", Phone Number: " + phoneNumber + ", Mobile Number: " + mobileNumber;
                    } else {
                        return null;
                    }
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
    public void save_level4Data(String Server_response, String signature) {
        Connection con = DBConnection.getConnection();
        if (con == null) {
            return;
        }
        String query = "INSERT INTO level4_data(Server_response,signature) VALUES (?,?);";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setString(1, Server_response);
            preparedStatement.setString(2, signature);
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
    public int get_id_level4data(String signature) {
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                return 0;
            }
            String query = "SELECT id FROM level4_data WHERE signature = ?;";
            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setString(1, signature);

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
    public void save_list_students_marks(String name, int mark,int fk) {
        Connection con = DBConnection.getConnection();
        if (con == null) {
            return;
        }
        String query = "INSERT INTO list_students_marks(name,mark,level4_data_id) VALUES (?,?,?);";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, mark);
            preparedStatement.setInt(3, fk);
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
}

