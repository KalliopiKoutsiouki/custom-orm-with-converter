package org.unipi.team.output;

import java.sql.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentGenerated {

   private String AM;

   private String email;

   private int yearOfStudies;

   private String fullName;

   private boolean postGraduate;

    public String getAM() {
        return this.AM;
    }

    public void setAM(String AM) {
        this.AM = AM;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getYearOfStudies() {
        return this.yearOfStudies;
    }

    public void setYearOfStudies(int yearOfStudies) {
        this.yearOfStudies = yearOfStudies;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean getPostGraduate() {
        return this.postGraduate;
    }

    public void setPostGraduate(boolean postGraduate) {
        this.postGraduate = postGraduate;
    }
    private static Connection connect() {
        String connectionString = "jdbc:h2:~/UnipiDB";
        String username = "sa";
        String password = "1234";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionString, username, password);
            System.out.println("Connection successful!");
        } catch (SQLException ex) {
            Logger.getLogger(StudentGenerated.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(StudentGenerated.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        createTable();
    }
}
