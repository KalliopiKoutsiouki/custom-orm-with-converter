package org.unipi.team.generator.annotationGenerator.impl;

import org.unipi.team.annotation.transaction.Field;
import org.unipi.team.annotation.transaction.Table;
import org.unipi.team.annotation.transaction.ID;
import org.unipi.team.generator.annotationGenerator.AnnotationCodeGenerator;

import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * Implementation of the {@link AnnotationCodeGenerator} interface for generating code
 * related to the {@link Table} annotation.
 */
public class TableAnnotationCodeGenerator implements AnnotationCodeGenerator {

    private StringBuilder sb;

    public TableAnnotationCodeGenerator(StringBuilder sb) {
        this.sb = sb;
    }

    /**
     * Generates the createTable method based on the provided {@link Table} annotation.
     *
     * @param sb          The StringBuilder used for appending generated code.
     * @param annotation  The {@link Table} annotation for which code should be generated.
     * @param className   The name of the class associated with the annotation.
     * @throws Exception  If an error occurs during the code generation process.
     */
    @Override
    public void generate(StringBuilder sb, Annotation annotation, String className) throws Exception {

        Class<?> clazz = Class.forName("org.unipi.team.input." + className );
        className = className + "Generated";
        if (annotation != null) {
            Table tableAnnotation = (Table) annotation;
            sb.append("    private static void createTable() {\n");
            sb.append("        try {\n");
            sb.append("            Connection connection = connect();\n");
            sb.append("            String sql = \"CREATE TABLE ")
                    .append(tableAnnotation.name())
                    .append(" (");

            java.lang.reflect.Field[] fields = clazz.getDeclaredFields();

            // Check for multiple @ID annotations
            long cntIdAnnotations = Arrays.stream(fields)
                    .filter(field -> field.isAnnotationPresent(ID.class))
                    .count();
            if (cntIdAnnotations <=1) {
                //Generate code for the fields of the table
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

    /**
     * Generates code for the fields of the table based on the {@link Field} annotation.
     *
     * @param field The field for which code should be generated.
     */
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
}
