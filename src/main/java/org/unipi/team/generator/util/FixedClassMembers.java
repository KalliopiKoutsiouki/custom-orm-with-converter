package org.unipi.team.generator.util;

import static org.unipi.team.generator.util.CommonImports.*;

public class FixedClassMembers {

    private static final String MAIN_EXECUTE_METHOD = "createTable()";
    private static final String PACKAGE = "package org.unipi.team.output;\n\n";
    public static void createClassHeader(StringBuilder sb, boolean hasDB) {
        sb.append(PACKAGE);
       if (hasDB) {
           sb.append(IMPORT_SQL_STATEMENT);
           sb.append(IMPORT_DRIVER_MANAGER);
           sb.append(IMPORT_SQL_EXCEPTION);
       }
        sb.append(IMPORT_LOGGING_LEVEL);
        sb.append(IMPORT_LOGGER);
    }

    public static void createClassDeclaration(StringBuilder sb, String className) {
        sb.append(String.format("public class %s {\n\n", className));
    }

    public static void closeClassDefinition(StringBuilder sb) {
        sb.append("}\n");
    }

    public static void generateMainWithExecute(StringBuilder sb, boolean createDb) {
        sb.append("    public static void main(String[] args) {\n");
        if (createDb) {
            sb.append("        " + MAIN_EXECUTE_METHOD + ";\n");
        }
        sb.append("    }\n");

    }

}
