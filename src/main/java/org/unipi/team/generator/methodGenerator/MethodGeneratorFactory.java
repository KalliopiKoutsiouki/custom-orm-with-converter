package org.unipi.team.generator.methodGenerator;

import org.unipi.team.annotation.transaction.DBMethod;

public class MethodGeneratorFactory {

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
