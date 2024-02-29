package org.unipi.team.generator;

import org.unipi.team.annotation.transaction.Database;
import org.unipi.team.generator.impl.DatabaseConnectionAnnotationCodeGenerator;
import org.unipi.team.generator.impl.TableAnnotationCodeGenerator;
import org.unipi.team.generator.util.FileGenerator;
import org.unipi.team.generator.util.FixedClassMembers;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class CodeGenerator {

    private static StringBuilder sb = new StringBuilder();
    private static String className;

    public static String compiledGettersSetters() {
        String filePath = "target/generated-sources/annotations/org/unipi/team/input/StudentGenerated.java";

        try {
            // Read the contents of the generated .java file
            String classString = new String(Files.readAllBytes(Paths.get(filePath)));
            classString.trim();
            int lastIndex = classString.length() - 1;
            // Remove the last character
            String removeClose = classString.substring(0, lastIndex-3);
//            String removeClose = classString.substring(0, classString.length() - 1);
            System.out.println(removeClose);
        return removeClose;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


        public static void generateCodeAndOutputFile(Class<?> clazz) {
        String fullClassName = clazz.getName();
        className = getNameWithoutPath(fullClassName);
        java.lang.annotation.Annotation[] annotations = clazz.getAnnotations();
        FixedClassMembers.createClassHeader(sb, clazz.isAnnotationPresent(Database.class));
//        FixedClassMembers.createClassDeclaration(sb,className);

        sb.append(compiledGettersSetters());
        if(clazz.isAnnotationPresent(Database.class)) {
            createDatabaseCode(annotations);
        }
        FixedClassMembers.generateMainWithExecute(sb, clazz.isAnnotationPresent(Database.class));
        FixedClassMembers.closeClassDefinition(sb);
        FileGenerator.generateOutputFile(sb, className);
    }

    private static void createDatabaseCode(Annotation[] annotations) {
        Arrays.stream(annotations)
                .sequential()
                .forEach(annotation -> {
                    AnnotationCodeGenerator generator = CodeGenerator.getGenerator(annotation);
                    try {
                        generator.generate(sb, annotation, className);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private static AnnotationCodeGenerator getGenerator(Annotation annotation) {
        String annotationName = getNameWithoutPath(annotation.annotationType().getName());
        try {
            switch (annotationName) {
                case "Database":
                    return new DatabaseConnectionAnnotationCodeGenerator(sb);
                case "Table":
                    return new TableAnnotationCodeGenerator(sb);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private static String getNameWithoutPath(String className) {
        int lastIndex = className.lastIndexOf('.');
        String substring = className.substring(lastIndex + 1);
        return substring;
    }
}
