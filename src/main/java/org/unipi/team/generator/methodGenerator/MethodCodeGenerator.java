package org.unipi.team.generator.methodGenerator;

import org.unipi.team.annotation.transaction.DBMethod;
import org.unipi.team.annotation.transaction.Field;
import org.unipi.team.annotation.transaction.Table;

import javax.management.RuntimeMBeanException;

/**
 * Abstract class representing a code generator for database operations based on the
 * {@link DBMethod} annotation. Its subclasses implement the generatedMethod method
 * to provide specific database operation logic.
 */
public abstract class MethodCodeGenerator {

    /**
     * Generates code for a specific database operation based on the provided class.
     * This method is abstract and is implemented by subclasses of MethodCodeGenerator.
     * Subclasses use this method to provide specific logic for database operations
     * associated with the given class.
     *
     * @param sb    The {@link StringBuilder} used for appending generated code.
     * @param clazz The class for which the database operation code is generated.
     */
    public abstract void generateMethod(StringBuilder sb, Class<?> clazz);

    /**
     * Generates code to set values of fields from a ResultSet to the corresponding
     * fields in the generated object of the given class.
     *
     * @param clazz The class for which the code is generated.
     * @param sb    The {@link StringBuilder} used for appending generated code.
     */
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

    /**
     * Retrieves the table name from the {@link Table} annotation of the provided class.
     * If the class is not annotated with {@link Table}, a {@link RuntimeException} is thrown.
     *
     * @param clazz The class for which the table name is to be retrieved.
     * @return The table name specified in the {@link Table} annotation.
     * @throws RuntimeException If the class is not annotated with {@link Table}.
     */
    protected String getTableName(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Table.class)) {
            Table tableAnnotation = clazz.getAnnotation(Table.class);
            return tableAnnotation.name();
        } else {
            throw new RuntimeException("Table annotation is missing for class: " + clazz.getSimpleName());
        }
    }

    /**
     * Capitalizes the first letter of a string.
     * This method is used to apply camelCase logic.
     *
     * @param str The input string.
     * @return The input string with the first letter capitalized.
     */
    protected String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
