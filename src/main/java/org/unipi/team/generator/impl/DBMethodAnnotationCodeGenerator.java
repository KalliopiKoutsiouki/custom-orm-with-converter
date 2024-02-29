package org.unipi.team.generator.impl;

import org.unipi.team.annotation.transaction.DBMethod;
import org.unipi.team.annotation.transaction.Field;
import org.unipi.team.annotation.transaction.Table;
import org.unipi.team.generator.AnnotationCodeGenerator;

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
            DBMethodAnnotationCodeGenerator codeGenerator;
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

    protected void generateMethod(StringBuilder sb, Class<?> clazz){
    }

    protected void generateSetValuesFromResultSet(Class<?> clazz, StringBuilder sb) {
        java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
        for (java.lang.reflect.Field field : fields) {
            if (field.isAnnotationPresent(Field.class)) {
                Field fieldAnnotation = field.getAnnotation(Field.class);
                String fieldName = field.getName();
                String fieldType = field.getType().getSimpleName();
                sb.append("                ").append(clazz.getSimpleName().toLowerCase()).append(".set").append(capitalize(fieldName))
                        .append("(resultSet.get").append(capitalize(fieldType))
                        .append("(\"").append(fieldAnnotation.name()).append("\"));\n");
            }
        }
    }
    protected String getTableName(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Table.class)) {
            Table tableAnnotation = clazz.getAnnotation(Table.class);
            return tableAnnotation.name();
        }
        return ""; //TODO: Handle exception
    }

    protected String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

}