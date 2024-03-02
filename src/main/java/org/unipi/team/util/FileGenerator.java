package org.unipi.team.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.unipi.team.App.OUTPUT_DIR;

public class FileGenerator {

    public static void generateOutputFile(StringBuilder sb, String className) {
        className = className + "Generated";
        File outputDir = new File(OUTPUT_DIR);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        File file = new File(OUTPUT_DIR + className + ".java");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
