package org.unipi.team.output;

import java.sql.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentGenerated {

   private String AM;

   private String email;

   private int yearOfStudies;

   private String fullName;

   private boolean postGraduate;

    public StudentGenerated(){};
    public StudentGenerated(String AM, String email, int yearOfStudies, String fullName, boolean postGraduate) {
        this.AM = AM;
        this.email = email;
        this.yearOfStudies = yearOfStudies;
        this.fullName = fullName;
        this.postGraduate = postGraduate;
    }

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
            String sql = "CREATE TABLE Student (AM TEXT PRIMARY KEY, Email TEXT NOT NULL, YearOfStudies INTEGER, FullName TEXT NOT NULL, PostGraduate BOOLEAN);";
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            connection.close();
            System.out.println("Table successfully created");
        } catch (SQLException ex) {
            Logger.getLogger(StudentGenerated.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static List<StudentGenerated> getAllStudentGenerateds() {
        try {
            Connection connection = connect();
            String sql = "SELECT * FROM Student";
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            List<StudentGenerated> resultList = new ArrayList<>();
            while (resultSet.next()) {
                StudentGenerated student = new StudentGenerated();
                student.setAM(resultSet.getString("AM"));
                student.setEmail(resultSet.getString("Email"));
                student.setYearOfStudies(resultSet.getInt("YearOfStudies"));
                student.setFullName(resultSet.getString("FullName"));
                student.setPostGraduate(resultSet.getBoolean("PostGraduate"));
                resultList.add(student);
            }
            resultSet.close();
            stmt.close();
            connection.close();
            return resultList;
        } catch (SQLException ex) {
            Logger.getLogger(StudentGenerated.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static int deleteStudentGenerated(String AM) {
        try {
            Connection connection = connect();
            String sql = "DELETE FROM Student WHERE AM = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, AM);
            int rowsAffected = pstmt.executeUpdate();
            pstmt.close();
            connection.close();
            System.out.println("Student deleted successfully");
            return rowsAffected;
        } catch (SQLException ex) {
            Logger.getLogger(StudentGenerated.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    public static StudentGenerated getStudentGeneratedByAM(String AM) {
        try {
            Connection connection = connect();
            String sql = "SELECT * FROM Student WHERE AM = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, AM);
            ResultSet resultSet = pstmt.executeQuery();
            StudentGenerated student = null;
            if (resultSet.next()) {
                student = new StudentGenerated();
                student.setAM(resultSet.getString("AM"));
                student.setEmail(resultSet.getString("Email"));
                student.setYearOfStudies(resultSet.getInt("YearOfStudies"));
                student.setFullName(resultSet.getString("FullName"));
                student.setPostGraduate(resultSet.getBoolean("PostGraduate"));
            }
            resultSet.close();
            pstmt.close();
            connection.close();
            return student;
        } catch (SQLException ex) {
            Logger.getLogger(StudentGenerated.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static void insertStudentGenerated(StudentGenerated student){
        try {
            Connection connection = connect();
            String sql = "INSERT INTO Student (AM, Email, YearOfStudies, FullName, PostGraduate) VALUES (?, ?, ?, ?, ?);";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, student.getAM());
            pstmt.setString(2, student.getEmail());
            pstmt.setInt(3, student.getYearOfStudies());
            pstmt.setString(4, student.getFullName());
            pstmt.setBoolean(5, student.getPostGraduate());
            pstmt.executeUpdate();
            pstmt.close();
            connection.close();
            System.out.println("Student inserted successfully");
        } catch (SQLException ex) {
            Logger.getLogger(StudentGenerated.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        createTable();
    }
}
