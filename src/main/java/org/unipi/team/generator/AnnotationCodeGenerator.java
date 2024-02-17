package org.unipi.team.generator;

import java.lang.annotation.Annotation;

public interface AnnotationCodeGenerator {

     void generate(StringBuilder sb, Annotation annotation, String className) throws Exception;

}
