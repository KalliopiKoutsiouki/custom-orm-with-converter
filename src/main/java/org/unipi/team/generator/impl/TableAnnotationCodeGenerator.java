package org.unipi.team.generator.impl;

import org.unipi.team.annotation.transaction.Field;
import org.unipi.team.annotation.transaction.Table;
import org.unipi.team.annotation.transaction.ID;
import org.unipi.team.generator.AnnotationCodeGenerator;

import java.lang.annotation.Annotation;
import java.util.Arrays;

public class TableAnnotationCodeGenerator implements AnnotationCodeGenerator {

    private StringBuilder sb;

    public TableAnnotationCodeGenerator(StringBuilder sb) {
        this.sb = sb;
    }

    @Override
    public void generate(StringBuilder sb, Annotation annotation, String className) throws Exception {

        Class<?> clazz = Class.forName("org.unipi.team.input." + className );
        className = className + "Generated";
        if (annotation != null) {
//            generateFields(clazz);
//            generateConstructors(clazz);
//            generateGettersAndSetters(clazz);
            Table tableAnnotation = (Table) annotation;
            sb.append("    private static void createTable() {\n");
            sb.append("        try {\n");
            sb.append("            Connection connection = connect();\n");
            sb.append("            String sql = \"CREATE TABLE ")
                    .append(tableAnnotation.name())
                    .append(" (");

            java.lang.reflect.Field[] fields = clazz.getDeclaredFields();

            //check for multiple @ID annotations
            long cntIdAnnotations = Arrays.stream(fields)
                    .filter(field -> field.isAnnotationPresent(ID.class))
                    .count();
            if (cntIdAnnotations <=1) {
                Arrays.stream(fields).sequential().forEach(field -> generateFieldsForTable(field));
            } else {
                throw new IllegalStateException("Multiple primary keys are not allowed!");
            }

            sb.setLength(sb.length() - 2); // Remove last comma and space
            sb.append(");\";\n");
            sb.append("            Statement stmt = connection.createStatement();\n");
            sb.append("            stmt.executeUpdate(sql);\n");

            // Close resources and class
            sb.append("            stmt.close();\n");
            sb.append("            connection.close();\n");
            sb.append("            System.out.println(\"Table successfully created\");\n");
            sb.append("        } catch (SQLException ex) {\n");
            sb.append(String.format("            Logger.getLogger(%s.class.getName()).log(Level.SEVERE, null, ex);\n", className));
            sb.append("        }\n");
            sb.append("    }\n\n");
        } else {
            throw new Exception("Annotation not found");
        }
    }

    private void generateFieldsForTable(java.lang.reflect.Field field) {
        if (field.isAnnotationPresent(Field.class)) {
            Field fieldAnnotation = field.getAnnotation(Field.class);
            sb.append(fieldAnnotation.name())
                    .append(" ")
                    .append(fieldAnnotation.type().toUpperCase());
            if (field.isAnnotationPresent(ID.class)) {
                sb.append(" PRIMARY KEY");
            }
            if (field.getAnnotation(Field.class).required()) {
                sb.append(" NOT NULL");
            }
            sb.append(", ");
        }
    }

//    private void generateFields(Class<?> clazz) {
//        java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
//        for (java.lang.reflect.Field field : fields) {
//            if (field.isAnnotationPresent(Field.class)) {
//                String fieldName = field.getName();
//                String fieldType = field.getType().getSimpleName();
//                sb.append(String.format("    private %s %s;\n", fieldType, fieldName));
//            }
//        }
//        sb.append("\n");
//    }
//
//    private void generateConstructors(Class<?> clazz) {
//        generateEmptyConstructor(clazz);
//        generateAllFieldsConstructor(clazz);
//    }
//
//    private void generateEmptyConstructor(Class<?> clazz) {
//        sb.append(String.format("    public %s() {\n", clazz.getSimpleName()));
//        sb.append("    }\n\n");
//    }
//
//    private void generateAllFieldsConstructor(Class<?> clazz) {
//        sb.append(String.format("    public %s(", clazz.getSimpleName()));
//        java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
//        for (java.lang.reflect.Field field : fields) {
//            if (field.isAnnotationPresent(Field.class)) {
//                String fieldName = field.getName();
//                String fieldType = field.getType().getSimpleName();
//                sb.append(String.format("%s %s, ", fieldType, fieldName));
//            }
//        }
//        // Remove the last comma and space if there are fields
//        if (fields.length > 0) {
//            sb.setLength(sb.length() - 2);
//        }
//        sb.append(") {\n");
//        // Initialize fields in the constructor
//        for (java.lang.reflect.Field field : fields) {
//            if (field.isAnnotationPresent(Field.class)) {
//                String fieldName = field.getName();
//                sb.append(String.format("        this.%s = %s;\n", fieldName, fieldName));
//            }
//        }
//        sb.append("    }\n\n");
//    }
//    private void generateGettersAndSetters(Class<?> clazz) {
//        java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
//        for (java.lang.reflect.Field field : fields) {
//            if (field.isAnnotationPresent(Field.class)) {
//                String fieldName = field.getName();
//                String fieldType = field.getType().getSimpleName();
//
//                // Generate getter method
//                sb.append(String.format("    public %s get%s() {\n", fieldType, capitalize(fieldName)));
//                sb.append(String.format("        return %s;\n", fieldName));
//                sb.append("    }\n\n");
//
//                // Generate setter method
//                sb.append(String.format("    public void set%s(%s %s) {\n", capitalize(fieldName), fieldType, fieldName));
//                sb.append(String.format("        this.%s = %s;\n", fieldName, fieldName));
//                sb.append("    }\n\n");
//            }
//        }
//    }

//    private String capitalize(String str) {
//        return str.substring(0, 1).toUpperCase() + str.substring(1);
//    }
}
