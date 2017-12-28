package com.codigine.easylog.annotation_processor;

import com.codigine.easylog.EasyLog;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

public class EasyLogAnnotationProcessor extends AbstractProcessor {

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        for (Element element : roundEnvironment.getElementsAnnotatedWith(EasyLog.class)) {

            if (element.getKind() != ElementKind.FIELD) {
                messager.printMessage(Diagnostic.Kind.ERROR, "EasyLog annotation must be applied to field, wrong annotated element: "
                        + element.getSimpleName() + " is " + element.getKind());
                return true;
            }

            try {
                AnnotatedElem elem = new AnnotatedElem((VariableElement) element);
                messager.printMessage(Diagnostic.Kind.OTHER, "typeElement " + elem.getClassElement().getQualifiedName());
                TypeSpec genrated = AspectGenerator.generateCode(elem);
                messager.printMessage(Diagnostic.Kind.OTHER, "Generated: " + genrated);
                GeneratedCodeWriter.save(filer, elementUtils, elem, genrated);
            } catch (IOException | IllegalArgumentException e) {
                messager.printMessage(Diagnostic.Kind.ERROR, "Error while processing annotation: " + e);
                return true;
            }

        }
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new HashSet<>();
        annotations.add("com.codigine.easylog.EasyLog");
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}