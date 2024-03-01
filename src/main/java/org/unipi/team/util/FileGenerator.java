package org.unipi.team.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileGenerator {

    public static void generateOutputFile(StringBuilder sb, String className) {
        className = className + "Generated";
        File file = new File("src/main/java/org/unipi/team/output/" + className + ".java");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
