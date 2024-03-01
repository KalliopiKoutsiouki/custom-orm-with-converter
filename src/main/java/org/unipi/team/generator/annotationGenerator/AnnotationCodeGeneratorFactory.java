package org.unipi.team.generator.annotationGenerator;

import org.unipi.team.generator.annotationGenerator.impl.DBMethodAnnotationCodeGenerator;
import org.unipi.team.generator.annotationGenerator.impl.DatabaseConnectionAnnotationCodeGenerator;
import org.unipi.team.generator.annotationGenerator.impl.TableAnnotationCodeGenerator;

public class AnnotationCodeGeneratorFactory {
    public static AnnotationCodeGenerator getGenerator(StringBuilder sb, String annotationName) {
        try {
            switch (annotationName) {
                case "Database":
                    return new DatabaseConnectionAnnotationCodeGenerator(sb);
                case "Table":
                    return new TableAnnotationCodeGenerator(sb);
                case "DBMethod":
                    return new DBMethodAnnotationCodeGenerator(sb);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
