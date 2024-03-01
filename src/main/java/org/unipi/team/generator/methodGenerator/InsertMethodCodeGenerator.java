package org.unipi.team.generator.methodGenerator;

import org.unipi.team.annotation.transaction.Field;

import java.util.Arrays;
import java.util.Date;

public class InsertMethodCodeGenerator extends MethodCodeGenerator {

    private String generatedClassName;
    public InsertMethodCodeGenerator(StringBuilder sb) {
    }

    @Override
    public void generateMethod(StringBuilder sb, Class<?> clazz) {
        generatedClassName = clazz.getSimpleName() + "Generated";
        sb.append("    public static void insert").append(generatedClassName).append("(").append(generatedClassName).append(" ").append(clazz.getSimpleName().toLowerCase()).append("){\n");
        sb.append("        try {\n");
        sb.append("            Connection connection = connect();\n");
        sb.append("            String sql = \"INSERT INTO ").append(getTableName(clazz)).append(" (");
        generateColumnNamesForInsert(clazz, sb);
        sb.append(") VALUES (");
        generateInsertPlaceholders(clazz, sb);
        sb.append(");\";\n");
        sb.append("            PreparedStatement pstmt = connection.prepareStatement(sql);\n");
        generateSetValuesForInsert(clazz, sb);
        sb.append("            pstmt.executeUpdate();\n");
        sb.append("            pstmt.close();\n");
        sb.append("            connection.close();\n");
        sb.append("            System.out.println(\"").append(clazz.getSimpleName()).append(" inserted successfully\");\n");
        sb.append("        } catch (SQLException ex) {\n");
        sb.append("            Logger.getLogger(").append(generatedClassName).append(".class.getName()).log(Level.SEVERE, null, ex);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    private void generateColumnNamesForInsert(Class<?> clazz, StringBuilder sb) {
        java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
        Arrays.stream(fields).sequential().forEach(field -> {
            if (field.isAnnotationPresent(Field.class)) {
                Field fieldAnnotation = field.getAnnotation(Field.class);
                sb.append(fieldAnnotation.name()).append(", ");
            }
        });
        sb.setLength(sb.length() - 2); // Remove last comma and space
    }

    private void generateInsertPlaceholders(Class<?> clazz, StringBuilder sb) {
        java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
        Arrays.stream(fields).sequential().forEach(field -> {
            if (field.isAnnotationPresent(Field.class)) {
                sb.append("?, ");
            }
        });
        sb.setLength(sb.length() - 2); // Remove last comma and space
    }

    private void generateSetValuesForInsert(Class<?> clazz, StringBuilder sb) {
        java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
        int parameterIndex = 1;
        for (java.lang.reflect.Field field : fields) {
            if (field.isAnnotationPresent(Field.class)) {
                sb.append(String.format("            pstmt.set%s(%d, %s.get%s());\n",
                        getPreparedStatementType(field.getType()),
                        parameterIndex++,
                        clazz.getSimpleName().toLowerCase(),
                        capitalize(field.getName())));
            }
        }
    }

    private String getPreparedStatementType(Class<?> fieldType) {
        if (fieldType == String.class) {
            return "String";
        } else if (fieldType == int.class || fieldType == Integer.class) {
            return "Int";
        } else if (fieldType == long.class || fieldType == Long.class) {
            return "Long";
        } else if (fieldType == double.class || fieldType == Double.class) {
            return "Double";
        } else if (fieldType == float.class || fieldType == Float.class) {
            return "Float";
        } else if (fieldType == boolean.class || fieldType == Boolean.class) {
            return "Boolean";
        } else if (fieldType == Date.class) {
            return "Date";
        } else {
            return "Object";
        }
    }

}
