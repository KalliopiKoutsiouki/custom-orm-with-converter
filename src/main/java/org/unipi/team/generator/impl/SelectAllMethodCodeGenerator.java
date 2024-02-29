package org.unipi.team.generator.impl;


import org.unipi.team.generator.MethodCodeGenerator;

public class SelectAllMethodCodeGenerator extends MethodCodeGenerator {
    public SelectAllMethodCodeGenerator(StringBuilder sb) {
    }

    @Override
    public void generateMethod(StringBuilder sb, Class<?> clazz) {
        String generatedClassName = clazz.getSimpleName() + "Generated";
        sb.append("    public static List<").append(generatedClassName).append("> getAll").append(generatedClassName).append("s() {\n");
        sb.append("        try {\n");
        sb.append("            Connection connection = connect();\n");
        sb.append("            String sql = \"SELECT * FROM ").append(getTableName(clazz)).append("\";\n");
        sb.append("            Statement stmt = connection.createStatement();\n");
        sb.append("            ResultSet resultSet = stmt.executeQuery(sql);\n");
        sb.append("            List<").append(generatedClassName).append("> resultList = new ArrayList<>();\n");
        sb.append("            while (resultSet.next()) {\n");
        sb.append("                ").append(generatedClassName).append(" ").append(clazz.getSimpleName().toLowerCase()).append(" = new ").append(generatedClassName).append("();\n");
        generateSetValuesFromResultSet(clazz, sb);
        sb.append("                resultList.add(").append(clazz.getSimpleName().toLowerCase()).append(");\n");
        sb.append("            }\n");
        sb.append("            resultSet.close();\n");
        sb.append("            stmt.close();\n");
        sb.append("            connection.close();\n");
        sb.append("            return resultList;\n");
        sb.append("        } catch (SQLException ex) {\n");
        sb.append("            Logger.getLogger(").append(generatedClassName).append(".class.getName()).log(Level.SEVERE, null, ex);\n");
        sb.append("            return null;\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
    }

}
