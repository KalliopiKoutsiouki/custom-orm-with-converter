package org.unipi.team.output;

import java.sql.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Student {

    private static Connection connect() {
        String connectionString = "jdbc:h2:~/UnipiDB";
        String username = "sa";
        String password = "1234";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionString, username, password);
            System.out.println("Connection successful!");
        } catch (SQLException ex) {
            Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connection;
    }

    private static void createTable() {
        try {
            Connection connection = connect();
            String sql = "CREATE TABLE Student (AM TEXT, Email TEXT, YearOfStudies INTEGER, FullName TEXT, PostGraduate BOOLEAN);";
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            connection.close();
            System.out.println("Table successfully created");
        } catch (SQLException ex) {
            Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        createTable();
    }
}
