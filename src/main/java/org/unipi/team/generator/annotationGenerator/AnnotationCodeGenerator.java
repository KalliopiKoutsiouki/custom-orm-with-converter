package org.unipi.team.generator.annotationGenerator;

import java.lang.annotation.Annotation;


/**
 * Interface for generating code based on annotations. Classes implementing this interface
 * are responsible for generating specific code logic associated with a particular annotation.
 */
public interface AnnotationCodeGenerator {


     void generate(StringBuilder sb, Annotation annotation, String className) throws Exception;

}
