package org.unipi.team;

import org.unipi.team.generator.CodeGenerator;
import org.unipi.team.input.Student;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) {
        try {
            CodeGenerator.generateCodeAndOutputFile(Student.class);
            System.out.println("Code generated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Code generation failed.");
        }
    }
}
