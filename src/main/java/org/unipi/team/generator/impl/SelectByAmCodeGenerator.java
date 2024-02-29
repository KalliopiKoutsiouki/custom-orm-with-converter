package org.unipi.team.generator.impl;

import org.unipi.team.generator.MethodCodeGenerator;

public class SelectByAmCodeGenerator extends MethodCodeGenerator {

    public SelectByAmCodeGenerator(StringBuilder sb) {
    }

    @Override
    public void generateMethod(StringBuilder sb, Class<?> clazz) {
        sb.append("    public static ").append(clazz.getSimpleName()).append(" get").append(clazz.getSimpleName()).append("ByAM(String AM) {\n");
        sb.append("        try {\n");
        sb.append("            Connection connection = connect();\n");
        sb.append("            String sql = \"SELECT * FROM ").append(getTableName(clazz)).append(" WHERE AM = ?\";\n");
        sb.append("            PreparedStatement pstmt = connection.prepareStatement(sql);\n");
        sb.append("            pstmt.setString(1, AM);\n");
        sb.append("            ResultSet resultSet = pstmt.executeQuery();\n");
        sb.append("            ").append(clazz.getSimpleName()).append(" ").append(clazz.getSimpleName().toLowerCase()).append(" = null;\n");
        sb.append("            if (resultSet.next()) {\n");
        sb.append("                ").append(clazz.getSimpleName().toLowerCase()).append(" = new ").append(clazz.getSimpleName()).append("();\n");
        generateSetValuesFromResultSet(clazz, sb);
        sb.append("            }\n");
        sb.append("            resultSet.close();\n");
        sb.append("            pstmt.close();\n");
        sb.append("            connection.close();\n");
        sb.append("            return ").append(clazz.getSimpleName().toLowerCase()).append(";\n");
        sb.append("        } catch (SQLException ex) {\n");
        sb.append("            Logger.getLogger(").append(clazz.getSimpleName()).append(".class.getName()).log(Level.SEVERE, null, ex);\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

}
