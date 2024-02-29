package org.unipi.team.generator.impl;

import org.unipi.team.annotation.transaction.DBMethod;
import org.unipi.team.annotation.transaction.Field;
import org.unipi.team.annotation.transaction.Table;
import org.unipi.team.generator.AnnotationCodeGenerator;
import org.unipi.team.generator.MethodCodeGenerator;

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
            MethodCodeGenerator codeGenerator;
            switch (dbMethodAnnotation.type()) {
                case "Insert":
                    codeGenerator = new InsertMethodCodeGenerator(sb);
                    break;
                case "SelectAll":
                    codeGenerator =  new SelectAllMethodCodeGenerator(sb);
                    break;
                case "SelectByAm":
                    codeGenerator = new SelectByAmCodeGenerator(sb);
                    break;
                case "DeleteOne":
                    codeGenerator = new DeleteOneMethodCodeGenerator(sb);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported DBMethod type: " + dbMethodAnnotation.type());
            }
            codeGenerator.generateMethod(sb, clazz);
        } else {
            throw new Exception("Annotation not found");
        }
    }

//    protected void generateMethod(StringBuilder sb, Class<?> clazz){
//    }

}