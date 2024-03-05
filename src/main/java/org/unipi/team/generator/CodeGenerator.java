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

// This class is responsible for generating code based on annotations
public class CodeGenerator {

    private static StringBuilder sb = new StringBuilder();
    private static final String FILEPATH = "target/generated-sources/annotations/org/unipi/team/input/StudentGenerated.java";
    private static String className;

    /**
     * Generates code and outputs a file based on the provided class.
     *
     * @param clazz The class for which code should be generated.
     */
    public static void generateCodeAndOutputFile(Class<?> clazz) {
        String fullClassName = clazz.getName();
        className = getNameWithoutPath(fullClassName);
        // Retrieve annotations present on the class using reflection
        java.lang.annotation.Annotation[] classAnnotations = clazz.getAnnotations();
        // Retrieve annotations present on the methods of the class using reflection
        java.lang.annotation.Annotation[] methodAnnotations = getMethodAnnotations(clazz);
        // Combine classAnnotations and methodAnnotations into a single array
        java.lang.annotation.Annotation[] annotations = Stream.concat(
                            Arrays.stream(classAnnotations),
                            Arrays.stream(methodAnnotations))
                    .toArray(java.lang.annotation.Annotation[]::new);
        // Check if the class has the DBMethod annotation
        boolean hasDBMethodAnnotation = Arrays.stream(annotations)
                    .anyMatch(annotation -> annotation.annotationType() == DBMethod.class);

        // Create the header of the generated class, including necessary imports
        FixedClassMembers.createClassHeader(sb, clazz.isAnnotationPresent(Database.class),hasDBMethodAnnotation);

        // Create the class definition, fields, constructors, getters, setters and toString method from the Compiled StudentGenerated.java file
        sb.append(readFromCompiledSourceAnnotations());

        // If the class has the Database annotation, generate code for it
        if (clazz.isAnnotationPresent(Database.class)) {
            createDatabaseCode(annotations);
        }
        // Generate the main method
        FixedClassMembers.generateMainWithExecute(sb, clazz.isAnnotationPresent(Database.class));
        // Close the class definition
        FixedClassMembers.closeClassDefinition(sb);
        // Generate the output file based on the generated code
        FileGenerator.generateOutputFile(sb, className);
    }

    /**
     * Reads the contents of the generated StudentGenerated.java file from the specified file path.
     * The contents of the StudentGenerated.java file are the class definition, fields, constructors,
     * getters, setters and toString method.
     *
     * Note: The StudentGenerated.java file is generated during compilation using the lombok_attempt dependency
     *
     * @return The contents of the generated StudentGenerated.java file as a {@link String}.
     * @throws RuntimeException If an IOException occurs during the file reading process.
     */
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


    /**
     * Creates code related to database operations based on the provided array of annotations.
     * Iterates through the array of annotations, extracts their names, and utilizes the
     * {@link AnnotationCodeGeneratorFactory} to generate corresponding code.
     *
     * @param annotations An array of annotations from which to generate database-related code.
     * @throws RuntimeException If an exception occurs during the code generation process.
     */
    private static void createDatabaseCode(Annotation[] annotations) {
        Arrays.stream(annotations)
                .sequential()
                .forEach(annotation -> {
                    // Extract the name of the annotation without the package path
                    String annotationName = getNameWithoutPath(annotation.annotationType().getName());
                    // Retrieve an AnnotationCodeGenerator based on the annotation name
                    AnnotationCodeGenerator generator = AnnotationCodeGeneratorFactory.getGenerator(sb, annotationName);
                    try {
                        // Generate code based on the annotation and append it to the StringBuilder
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
