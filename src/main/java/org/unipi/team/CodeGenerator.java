package org.unipi.team;

import org.unipi.team.annotation.transaction.Database;
import org.unipi.team.annotation.transaction.Field;
import org.unipi.team.annotation.transaction.Table;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CodeGenerator {
    public static void generateDatabaseCode(Class<?> clazz) {
        StringBuilder sb = new StringBuilder();
        if (clazz.isAnnotationPresent(Database.class)) {
            Database dbAnnotation = clazz.getAnnotation(Database.class);

            // Start of the class definition
            sb.append("package org.unipi.team.output;\n\n");
            sb.append("import java.sql.*;\n");
            sb.append("import java.sql.DriverManager;\n");
            sb.append("import java.sql.SQLException;\n");
            sb.append("import java.util.logging.Level;\n");
            sb.append("import java.util.logging.Logger;\n\n");
            sb.append("public class GeneratedCode {\n\n");

            // Generate the connect method
            sb.append("    private static Connection connect() {\n");
            sb.append("        String connectionString = \"jdbc:");


            // Append the correct JDBC URL based on DataSource
            switch (dbAnnotation.dbtype()) {
                case DERBY:
                    sb.append("derby:").append(dbAnnotation.name());
                    break;
                case MYSQL:
                    sb.append("mysql://localhost/").append(dbAnnotation.name());
                    break;
                case MARIADB:
                    sb.append("mariadb://localhost/").append(dbAnnotation.name());
                    break;
                default:
                    sb.append("h2:~/").append(dbAnnotation.name());
                    break;
            }
            sb.append("\"");
            sb.append(";\n");
            sb.append("        String username = \"")
                    .append(dbAnnotation.username())
                    .append("\";\n");
            sb.append("        String password = \"")
                    .append(dbAnnotation.password())
                    .append("\";\n");
            sb.append("        Connection connection = null;\n");
            sb.append("        try {\n");
            sb.append("            connection = DriverManager.getConnection(connectionString, username, password);\n");
            sb.append("            System.out.println(\"Connection successful!\");\n");
            sb.append("        } catch (SQLException ex) {\n");
            sb.append("            Logger.getLogger(GeneratedCode.class.getName()).log(Level.SEVERE, null, ex);\n");
            sb.append("        }\n");
            sb.append("        return connection;\n");
            sb.append("    }\n\n");

        }
        // Generate the createTable method
        if(clazz.isAnnotationPresent(Table.class)) {
            Table tableAnnotation = clazz.getAnnotation(Table.class);
            sb.append("    private static void createTable() {\n");
            sb.append("        try {\n");
            sb.append("            Connection connection = connect();\n");
            sb.append("            String sql = \"CREATE TABLE ")
                    .append(tableAnnotation.name())
                    .append(" (");

            java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
            for (java.lang.reflect.Field field : fields) {
                if (field.isAnnotationPresent(Field.class)) {
                    Field fieldAnnotation = field.getAnnotation(Field.class);
                    sb.append(fieldAnnotation.name())
                            .append(" ")
                            .append(fieldAnnotation.type().toUpperCase())
                            .append(", ");
                }
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
            sb.append("            Logger.getLogger(GeneratedCode.class.getName()).log(Level.SEVERE, null, ex);\n");
            sb.append("        }\n");
            sb.append("    }\n\n");
        }

        // Generate the main method
        sb.append("    public static void main(String[] args) {\n");
        sb.append("        createTable();\n");
        sb.append("    }\n");

        // Close the class definition
        sb.append("}\n");

        // Write the generated code to a .java file
        File file = new File("src/main/java/org/unipi/team/output/GeneratedCode.java");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
