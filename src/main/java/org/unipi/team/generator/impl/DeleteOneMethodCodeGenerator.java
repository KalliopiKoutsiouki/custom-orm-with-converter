package org.unipi.team.generator.impl;

import org.unipi.team.generator.MethodCodeGenerator;

public class DeleteOneMethodCodeGenerator extends MethodCodeGenerator {
    public DeleteOneMethodCodeGenerator(StringBuilder sb) {
    }

    @Override
    public void generateMethod(StringBuilder sb, Class<?> clazz) {
        String generatedClassName = clazz.getSimpleName() + "Generated";
        sb.append("    public static int delete").append(generatedClassName).append("(String AM) {\n");
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
        sb.append("            Logger.getLogger(").append(generatedClassName).append(".class.getName()).log(Level.SEVERE, null, ex);\n");
        sb.append("            return 0;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }


}
