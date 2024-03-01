package org.unipi.team.generator.annotationGenerator.impl;

import org.unipi.team.annotation.transaction.DBMethod;
import org.unipi.team.generator.annotationGenerator.AnnotationCodeGenerator;
import org.unipi.team.generator.methodGenerator.MethodCodeGenerator;
import org.unipi.team.generator.methodGenerator.MethodGeneratorFactory;

import java.lang.annotation.Annotation;


public class DBMethodAnnotationCodeGenerator implements AnnotationCodeGenerator {

    private StringBuilder sb;

    public DBMethodAnnotationCodeGenerator(StringBuilder sb) {
        this.sb = sb;
    }

    @Override
    public void generate(StringBuilder sb, Annotation annotation, String className) throws Exception {
        Class<?> clazz = Class.forName("org.unipi.team.input." + className);
        if (annotation != null) {
            DBMethod dbMethodAnnotation = (DBMethod) annotation;
            MethodCodeGenerator codeGenerator = MethodGeneratorFactory.methodGenerator(sb,dbMethodAnnotation);
            codeGenerator.generateMethod(sb, clazz);
        } else {
            throw new Exception("Annotation not found");
        }
    }

}