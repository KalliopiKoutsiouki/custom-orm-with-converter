package org.unipi.team.util;

import static org.unipi.team.App.OUTPUT_DIR;
import static org.unipi.team.util.CommonImports.*;

public class FixedClassMembers {

    private static final String MAIN_EXECUTE_METHOD = "createTable()";
    private static final String PACKAGE = "package org.unipi.team." + getOutputDir() + ";\n\n";
    public static void createClassHeader(StringBuilder sb, boolean hasDB, boolean hasDBMethodAnnotation) {
       sb.append(PACKAGE);
       if (hasDB) {
           sb.append(IMPORT_SQL_STATEMENT);
           sb.append(IMPORT_DRIVER_MANAGER);
           sb.append(IMPORT_SQL_EXCEPTION);
       }
       if (hasDBMethodAnnotation){
           sb.append(IMPORT_LIST);
           sb.append(IMPORT_ARRAYLIST);
       }
        sb.append(IMPORT_LOGGING_LEVEL);
        sb.append(IMPORT_LOGGER);
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

    private static String getOutputDir() {
        String directory = OUTPUT_DIR;
        String[] parts = directory.split("/");
        return parts[parts.length - 1];
    }


}
