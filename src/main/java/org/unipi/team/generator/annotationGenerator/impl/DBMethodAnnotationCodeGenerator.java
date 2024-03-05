package org.unipi.team.generator.annotationGenerator.impl;

import org.unipi.team.annotation.transaction.DBMethod;
import org.unipi.team.generator.annotationGenerator.AnnotationCodeGenerator;
import org.unipi.team.generator.methodGenerator.MethodCodeGenerator;
import org.unipi.team.generator.methodGenerator.MethodGeneratorFactory;

import java.lang.annotation.Annotation;

/**
 * Implementation of the {@link AnnotationCodeGenerator} interface for generating code
 * related to the {@link DBMethod} annotation.
 * This class acts as a 'bridge' between the generic annotation-driven code generation defined in {@link AnnotationCodeGenerator}
 * and the specific database operation logic implemented by classes that extend {@link MethodCodeGenerator}.
 */
public class DBMethodAnnotationCodeGenerator implements AnnotationCodeGenerator {

    private StringBuilder sb;

    public DBMethodAnnotationCodeGenerator(StringBuilder sb) {
        this.sb = sb;
    }

    /**
     * Generates code based on the provided {@link DBMethod} annotation, delegating the method
     * generation to an appropriate {@link MethodCodeGenerator}.
     *
     * @param sb          The StringBuilder used for appending generated code.
     * @param annotation  The {@link DBMethod} annotation for which code should be generated.
     * @param className   The name of the class associated with the annotation.
     * @throws Exception  If an error occurs during the code generation process.
     */
    @Override
    public void generate(StringBuilder sb, Annotation annotation, String className) throws Exception {
        Class<?> clazz = Class.forName("org.unipi.team.input." + className);
        if (annotation != null) {
            DBMethod dbMethodAnnotation = (DBMethod) annotation;
            // Use the MethodGeneratorFactory to get the appropriate code generator
            MethodCodeGenerator codeGenerator = MethodGeneratorFactory.methodGenerator(sb,dbMethodAnnotation);
            codeGenerator.generateMethod(sb, clazz);
        } else {
            throw new Exception("Annotation not found");
        }
    }

}