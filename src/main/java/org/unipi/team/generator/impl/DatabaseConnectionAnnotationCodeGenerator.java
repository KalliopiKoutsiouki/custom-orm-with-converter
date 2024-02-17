package org.unipi.team.generator.impl;

import org.unipi.team.annotation.transaction.Database;
import org.unipi.team.generator.AnnotationCodeGenerator;

import java.lang.annotation.Annotation;

public class DatabaseConnectionAnnotationCodeGenerator implements AnnotationCodeGenerator {

    private StringBuilder sb;
    public DatabaseConnectionAnnotationCodeGenerator(StringBuilder sb) {
        this.sb = sb;
    }

    @Override
    public void generate(StringBuilder sb,  Annotation annotation, String className) throws Exception {
        if (annotation != null) {
            Database dbAnnotation = (Database) annotation;
            sb.append("    private static Connection connect() {\n");
            sb.append("        String connectionString = \"jdbc:");
            configureJdbcDriver(dbAnnotation);
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
            sb.append(String.format("            Logger.getLogger(%s.class.getName()).log(Level.SEVERE, null, ex);\n", className));
            sb.append("        }\n");
            sb.append("        return connection;\n");
            sb.append("    }\n\n");
        } else {
            throw new Exception("Annotation not found");
        }

    }

    private void configureJdbcDriver(Database dbAnnotation) {
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
    }

}
