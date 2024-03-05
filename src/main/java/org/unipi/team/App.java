package org.unipi.team;

import org.unipi.team.generator.CodeGenerator;
import org.unipi.team.input.Student;

public class App 
{
    // Output directory for generated code
    public static final String OUTPUT_DIR= "src/main/java/org/unipi/team/output/";
    public static void main(String[] args) {
        try {
            // Generate code and output file for the Student class
            CodeGenerator.generateCodeAndOutputFile(Student.class);
            System.out.println("Code generated successfully at directory: " + OUTPUT_DIR);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Code generation failed");
        }
    }
}
