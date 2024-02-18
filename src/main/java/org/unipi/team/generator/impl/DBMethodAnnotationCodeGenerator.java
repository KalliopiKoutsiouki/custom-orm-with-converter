package org.unipi.team.generator.impl;

import org.unipi.team.annotation.transaction.DBMethod;
import org.unipi.team.annotation.transaction.Field;
import org.unipi.team.annotation.transaction.Table;
import org.unipi.team.generator.AnnotationCodeGenerator;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Date;

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
            switch (dbMethodAnnotation.type()) {
                case "Insert":
                    generateInsertMethod(clazz);
                    break;
                case "SelectAll":
                    generateSelectAllMethod(clazz);
                    break;
                case "DeleteOne":
                    generateDeleteOneMethod(clazz);
                    break;
            }
        } else {
            throw new Exception("Annotation not found");
        }
    }

    private void generateInsertMethod(Class<?> clazz) {
        sb.append("    public static void insert").append(clazz.getSimpleName()).append("(").append(clazz.getSimpleName()).append(" ").append(clazz.getSimpleName().toLowerCase()).append("){\n");
        sb.append("        try {\n");
        sb.append("            Connection connection = connect();\n");
        sb.append("            String sql = \"INSERT INTO ").append(getTableName(clazz)).append(" (");
        generateColumnNamesForInsert(clazz);
        sb.append(") VALUES (");
        generateInsertPlaceholders(clazz);
        sb.append(");\";\n");
        sb.append("            PreparedStatement pstmt = connection.prepareStatement(sql);\n");
        generateSetValuesForInsert(clazz);
        sb.append("            pstmt.executeUpdate();\n");
        sb.append("            pstmt.close();\n");
        sb.append("            connection.close();\n");
        sb.append("            System.out.println(\"").append(clazz.getSimpleName()).append(" inserted successfully\");\n");
        sb.append("        } catch (SQLException ex) {\n");
        sb.append("            Logger.getLogger(").append(clazz.getSimpleName()).append(".class.getName()).log(Level.SEVERE, null, ex);\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    private void generateColumnNamesForInsert(Class<?> clazz) {
        java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
        Arrays.stream(fields).sequential().forEach(field -> {
            if (field.isAnnotationPresent(Field.class)) {
                Field fieldAnnotation = field.getAnnotation(Field.class);
                sb.append(fieldAnnotation.name()).append(", ");
            }
        });
        sb.setLength(sb.length() - 2); // Remove last comma and space
    }

    private void generateInsertPlaceholders(Class<?> clazz) {
        java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
        Arrays.stream(fields).sequential().forEach(field -> {
            if (field.isAnnotationPresent(Field.class)) {
                sb.append("?, ");
            }
        });
        sb.setLength(sb.length() - 2); // Remove last comma and space
    }
    private void generateSetValuesForInsert(Class<?> clazz) {
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

    private void generateSelectAllMethod(Class<?> clazz) {
        sb.append("    public static List<").append(clazz.getSimpleName()).append("> getAll").append(clazz.getSimpleName()).append("s() {\n");
        sb.append("        try {\n");
        sb.append("            Connection connection = connect();\n");
        sb.append("            String sql = \"SELECT * FROM ").append(getTableName(clazz)).append("\";\n");
        sb.append("            Statement stmt = connection.createStatement();\n");
        sb.append("            ResultSet resultSet = stmt.executeQuery(sql);\n");
        sb.append("            List<").append(clazz.getSimpleName()).append("> resultList = new ArrayList<>();\n");
        sb.append("            while (resultSet.next()) {\n");
        sb.append("                ").append(clazz.getSimpleName()).append(" ").append(clazz.getSimpleName().toLowerCase()).append(" = new ").append(clazz.getSimpleName()).append("();\n");
        generateSetValuesFromResultSet(clazz);
        sb.append("                resultList.add(").append(clazz.getSimpleName().toLowerCase()).append(");\n");
        sb.append("            }\n");
        sb.append("            resultSet.close();\n");
        sb.append("            stmt.close();\n");
        sb.append("            connection.close();\n");
        sb.append("            return resultList;\n");
        sb.append("        } catch (SQLException ex) {\n");
        sb.append("            Logger.getLogger(").append(clazz.getSimpleName()).append(".class.getName()).log(Level.SEVERE, null, ex);\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    private void generateSetValuesFromResultSet(Class<?> clazz) {
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

    private void generateDeleteOneMethod(Class<?> clazz) {
        sb.append("    public static int delete").append(clazz.getSimpleName()).append("(String AM) {\n");
        sb.append("        try {\n");
        sb.append("            Connection connection = connect();\n");
        sb.append("            String sql = \"DELETE FROM ").append(getTableName(clazz)).append(" WHERE AM = ?\";\n");
        sb.append("            PreparedStatement pstmt = connection.prepareStatement(sql);\n");
        sb.append("            pstmt.setString(1, AM);\n");
        sb.append("            int rowsAffected = pstmt.executeUpdate();\n");
        sb.append("            pstmt.close();\n");
        sb.append("            connection.close();\n");
        sb.append("            System.out.println(\"").append(clazz.getSimpleName()).append(" deleted successfully\");\n");
        sb.append("            return rowsAffected;\n");
        sb.append("        } catch (SQLException ex) {\n");
        sb.append("            Logger.getLogger(").append(clazz.getSimpleName()).append(".class.getName()).log(Level.SEVERE, null, ex);\n");
        sb.append("            return 0;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

    private String getTableName(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Table.class)) {
            Table tableAnnotation = clazz.getAnnotation(Table.class);
            return tableAnnotation.name();
        }
        return ""; //TODO: Handle exception
    }

    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

}