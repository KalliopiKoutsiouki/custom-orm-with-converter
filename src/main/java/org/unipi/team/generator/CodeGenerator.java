package org.unipi.team.generator;

import org.unipi.team.annotation.transaction.DBMethod;
import org.unipi.team.annotation.transaction.Database;
import org.unipi.team.generator.annotationGenerator.AnnotationCodeGenerator;
import org.unipi.team.generator.annotationGenerator.AnnotationCodeGeneratorFactory;
import org.unipi.team.util.FileGenerator;
import org.unipi.team.util.FixedClassMembers;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CodeGenerator {

    private static StringBuilder sb = new StringBuilder();
    private static final String FILEPATH = "target/generated-sources/annotations/org/unipi/team/input/StudentGenerated.java";
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

        FixedClassMembers.createClassHeader(sb, clazz.isAnnotationPresent(Database.class),hasDBMethodAnnotation);

        sb.append(readFromCompiledSourceAnnotations());
        if(clazz.isAnnotationPresent(Database.class)) {
            createDatabaseCode(annotations);
        }
        FixedClassMembers.generateMainWithExecute(sb, clazz.isAnnotationPresent(Database.class));
        FixedClassMembers.closeClassDefinition(sb);
        FileGenerator.generateOutputFile(sb, className);
    }

    // getters, setters, constructors
    public static String readFromCompiledSourceAnnotations() {
        try {
            // Read the contents of the generated .java file
            String classString = new String(Files.readAllBytes(Paths.get(FILEPATH)));
            classString.trim();
            int closingBracket = classString.length() - 4;
            String removeClose = classString.substring(0, closingBracket);
            return removeClose;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
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
                    String annotationName = getNameWithoutPath(annotation.annotationType().getName());
                    AnnotationCodeGenerator generator = AnnotationCodeGeneratorFactory.getGenerator(sb, annotationName);
                    try {
                        generator.generate(sb, annotation, className);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private static String getNameWithoutPath(String className) {
        int lastIndex = className.lastIndexOf('.');
        String substring = className.substring(lastIndex + 1);
        return substring;
    }
}
