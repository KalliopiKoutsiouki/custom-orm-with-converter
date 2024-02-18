package org.unipi.team.generator;

import org.unipi.team.annotation.transaction.DBMethod;
import org.unipi.team.annotation.transaction.Database;
import org.unipi.team.generator.impl.DBMethodAnnotationCodeGenerator;
import org.unipi.team.generator.impl.DatabaseConnectionAnnotationCodeGenerator;
import org.unipi.team.generator.impl.TableAnnotationCodeGenerator;
import org.unipi.team.generator.util.FileGenerator;
import org.unipi.team.generator.util.FixedClassMembers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CodeGenerator {

    private static StringBuilder sb = new StringBuilder();
    private static String className;

    public static void generateCodeAndOutputFile(Class<?> clazz) {
        String fullClassName = clazz.getName();
        className = getNameWithoutPath(fullClassName);
        java.lang.annotation.Annotation[] classAnnotations = clazz.getAnnotations();
        java.lang.annotation.Annotation[] methodAnnotations = getMethodAnnotations(clazz);

        // Combine classAnnotations and methodAnnotations into a single array
        java.lang.annotation.Annotation[] annotations = Stream.concat(
                        Arrays.stream(classAnnotations),
                        Arrays.stream(methodAnnotations))
                .toArray(java.lang.annotation.Annotation[]::new);

        boolean hasDBMethodAnnotation = Arrays.stream(annotations)
                .anyMatch(annotation -> annotation.annotationType() == DBMethod.class);
        FixedClassMembers.createClassHeader(sb, clazz.isAnnotationPresent(Database.class), hasDBMethodAnnotation);
        FixedClassMembers.createClassDeclaration(sb,className);
        if(clazz.isAnnotationPresent(Database.class)) {
            createDatabaseCode(annotations);
        }
        FixedClassMembers.generateMainWithExecute(sb, clazz.isAnnotationPresent(Database.class));
        FixedClassMembers.closeClassDefinition(sb);
        FileGenerator.generateOutputFile(sb, className);
    }

    private static Annotation[] getMethodAnnotations (Class<?> clazz){
        Method[] methods = clazz.getDeclaredMethods();
        List<Annotation> allMethodAnnotations = Arrays.stream(methods)
                .map(Method::getAnnotations)
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());
        return allMethodAnnotations.toArray(new Annotation[0]);
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
                case "DBMethod":
                    return new DBMethodAnnotationCodeGenerator(sb);
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
