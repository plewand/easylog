package com.codigine.easylog.annotation_processor;

import com.codigine.easylog.EasyLog;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import javax.lang.model.element.Modifier;


public class AspectGenerator {
    private AspectGenerator() {

    }

    static TypeSpec generateCode(AnnotatedElem annotatedElem) {
        return generateAspectClass(annotatedElem);
    }

    private static TypeSpec generateAspectClass(AnnotatedElem annotatedElem) {
        String aspectName = annotatedElem.getClassElement().getQualifiedName().toString().replace(".", "_")
                + annotatedElem.getFieldElem().getSimpleName();
        return TypeSpec.classBuilder(aspectName)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(generateJoinPoint(annotatedElem))
                .addAnnotation(Aspect.class)
                .build();
    }

    private static MethodSpec generateJoinPoint(AnnotatedElem annotatedElem) {
        String qualifiedName = annotatedElem.getClassElement().getQualifiedName().toString();
        String packageName = "";
        String className = qualifiedName;
        int id = qualifiedName.lastIndexOf(".");
        if (id >= 0) {
            packageName = qualifiedName.substring(0, id);
            className = qualifiedName.substring(id + 1, qualifiedName.length());
        }

        return MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(JoinPoint.class, "joinPoint")
                .addParameter(ClassName.get(packageName, className), "o")
                .addStatement(buildFieldUpdateStatemnt(annotatedElem))
                .addException(Exception.class)
                .addAnnotation(AnnotationSpec.builder(Before.class)
                        .addMember("value", "\"initialization(" + qualifiedName + ".new(..)) && this(o)\"")
                        .build())
                .build();
    }

    private static String buildFieldUpdateStatemnt(AnnotatedElem annotatedElem) {
        String template = "java.lang.reflect.Field declaredField = %s.class.getDeclaredField(\"%s\");\n" +
                "            boolean accessible = declaredField.isAccessible();\n" +
                "            declaredField.setAccessible(true);\n" +
                "            declaredField.set(o, com.codigine.easylog.Logging.newLog(\"%s\", \"%s\", %b, %b));\n" +
                "            declaredField.setAccessible(accessible)";
        EasyLog annotation = annotatedElem.getAnnotation();
        return String.format(template,
                annotatedElem.getClassElement().getQualifiedName(),
                annotatedElem.getFieldElem().getSimpleName().toString().replaceAll("\\$", "\\$\\$"),
                annotation.tag() == null || annotation.tag().trim().isEmpty() ? annotatedElem.getClassElement().getSimpleName() : annotation.tag(),
                annotation.fileLogPath(),
                annotation.logcatEnabled(),
                annotation.fileLoggingEnabled());
    }
}
