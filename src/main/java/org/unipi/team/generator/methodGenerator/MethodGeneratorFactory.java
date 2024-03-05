package org.unipi.team.generator.methodGenerator;

import org.unipi.team.annotation.transaction.DBMethod;

/**
 * Factory class responsible for creating instances of classes
 * that extend {@link MethodCodeGenerator} abstract class based on the provided {@link DBMethod} annotation.
 * It supports 4 types of database operations (Insert, SelectAll, SelectByAm, DeleteOne)
 * and each operation has its specific code generator.
 */
public class MethodGeneratorFactory {

    /**
     * Creates an instance of a class that extend the {@link MethodCodeGenerator} abstract class
     * based on the provided {@link DBMethod} annotation.
     *
     * @param sb                 The StringBuilder used for appending generated code.
     * @param dbMethodAnnotation The {@link DBMethod} annotation specifying the type of operation.
     * @return An instance of the appropriate {@link MethodCodeGenerator}.
     * @throws IllegalArgumentException If the provided DBMethod type is not supported.
     */
    public static MethodCodeGenerator methodGenerator(StringBuilder sb, DBMethod dbMethodAnnotation) {
        switch (dbMethodAnnotation.type()) {
            case "Insert":
                return new InsertMethodCodeGenerator(sb);
            case "SelectAll":
                return new SelectAllMethodCodeGenerator(sb);
            case "SelectByAm":
                return new SelectByAmCodeGenerator(sb);
            case "DeleteOne":
                return new DeleteOneMethodCodeGenerator(sb);
            default:
                throw new IllegalArgumentException("Unsupported DBMethod type: " + dbMethodAnnotation.type());
        }
    }
}
