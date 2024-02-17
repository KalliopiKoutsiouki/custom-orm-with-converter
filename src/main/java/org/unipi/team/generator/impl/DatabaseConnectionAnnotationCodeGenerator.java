package org.unipi.team.generator.impl;

import org.unipi.team.annotation.transaction.Database;
import org.unipi.team.generator.AnnotationCodeGenerator;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Properties;

public class DatabaseConnectionAnnotationCodeGenerator implements AnnotationCodeGenerator {

    private StringBuilder sb;
    private  Properties properties = new Properties();
    public DatabaseConnectionAnnotationCodeGenerator(StringBuilder sb) {
        this.sb = sb;
        this.properties = getDBCredentials();
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
                    .append(properties.get("datasource.username"))
                    .append("\";\n");
            sb.append("        String password = \"")
                    .append(properties.get("datasource.password"))
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

    private Properties getDBCredentials() {
        try {
            properties.load(DatabaseConnectionAnnotationCodeGenerator.class.getResourceAsStream("/connection.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
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
