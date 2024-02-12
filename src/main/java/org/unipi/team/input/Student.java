package org.unipi.team.input;

import org.unipi.team.annotation.consts.DataSource;
import org.unipi.team.annotation.source.GenerateCompiledFile;
import org.unipi.team.annotation.transaction.*;

import java.util.List;
//@GenerateCompiledFile()
@Database(name="UnipiDB",dbtype= DataSource.H2, username = "sa", password = "1234")
@Table(name="Student")
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


    //Για τη μέθοδο αυτή μπορείτε να δοκιμάστε να επιστρέφετε
    //List<Student>
    @DBMethod(type="SelectAll")
    public List<String> getAllStudents(){
        return null;
    }


    //Ο επιστρεφόμενος ακέραιος υποδηλώνει τον αριθμό των εγγραφών που διαγράφηκαν
    @DBMethod(type="DeleteOne")
    public int deleteStudents(String AM){
        return 0;
    }
}

