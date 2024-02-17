package org.unipi.team.generator.impl;

import org.unipi.team.annotation.transaction.Field;
import org.unipi.team.annotation.transaction.Table;
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
        Class<?> clazz = Class.forName("org.unipi.team.input." + className);
        if (annotation != null) {
            Table tableAnnotation = (Table) annotation;
            sb.append("    private static void createTable() {\n");
            sb.append("        try {\n");
            sb.append("            Connection connection = connect();\n");
            sb.append("            String sql = \"CREATE TABLE ")
                    .append(tableAnnotation.name())
                    .append(" (");

            java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
            Arrays.stream(fields).sequential().forEach(this::generateFieldsForTable);
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
                    .append(fieldAnnotation.type().toUpperCase())
                    .append(", ");
        }
    }
}
