package org.unipi.team.generator;

import org.unipi.team.annotation.transaction.Field;
import org.unipi.team.annotation.transaction.Table;

public abstract class MethodCodeGenerator {
    public abstract void generateMethod(StringBuilder sb, Class<?> clazz);

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
