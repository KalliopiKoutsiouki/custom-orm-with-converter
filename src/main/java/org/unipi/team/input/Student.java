package org.unipi.team.input;



import org.example.annotation.ArgsConstructor;
import org.example.annotation.Getters;
import org.example.annotation.Setters;
import org.example.annotation.ToString;
import org.unipi.team.annotation.consts.DataSource;
import org.unipi.team.annotation.transaction.*;

import java.util.List;
@Database(name="UnipiDB",dbtype= DataSource.H2)
@Table(name="Student")
@Getters
@Setters
@ArgsConstructor
@ToString
public class Student {
    @ID
    @Field(name="AM",type="Text")
    String AM;
    @Field(name="Email",type="Text", required=true)
    String email;
    @Field(name="YearOfStudies",type="Integer")
    int yearOfStudies;
    @Field(name="FullName",type="Text", required=true)
    String fullName;
    @Field(name="PostGraduate",type="Boolean")
    boolean postGraduate;


    @DBMethod(type="Insert")
    public void insertStudent() { }

    //Για τη μέθοδο αυτή μπορείτε να δοκιμάστε να επιστρέφετε
    //List<Student>
    @DBMethod(type="SelectAll")
    public List<Student> getAllStudents(){
        return null;
    }


    //Ο επιστρεφόμενος ακέραιος υποδηλώνει τον αριθμό των εγγραφών που διαγράφηκαν
    @DBMethod(type="DeleteOne")
    public int deleteStudents(String AM){
        return 0;
    }

    @DBMethod(type ="SelectByAm")
    public Student getStudentByAm(String AM) {
        return null;
    }
}
