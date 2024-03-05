package org.unipi.team.generator.annotationGenerator;

import org.unipi.team.generator.annotationGenerator.impl.DBMethodAnnotationCodeGenerator;
import org.unipi.team.generator.annotationGenerator.impl.DatabaseConnectionAnnotationCodeGenerator;
import org.unipi.team.generator.annotationGenerator.impl.TableAnnotationCodeGenerator;


public class AnnotationCodeGeneratorFactory {

    /**
     * Retrieves an instance of a class implementing {@link AnnotationCodeGenerator} based on the provided
     * annotation name. It supports the following annotations:
     * - Database
     * - Table
     * - DBMethod
     *
     * @param sb              The StringBuilder used for appending generated code.
     * @param annotationName  The name of the annotation for which a code generator is requested.
     * @return An instance of a class implementing {@link AnnotationCodeGenerator} corresponding to
     *         the provided annotation name.
     */
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
