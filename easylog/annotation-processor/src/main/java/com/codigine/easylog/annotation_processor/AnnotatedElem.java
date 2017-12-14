package com.codigine.easylog.annotation_processor;

import com.codigine.easylog.EasyLog;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

class AnnotatedElem {
    private VariableElement fieldElem;
    private TypeElement classElement;
    private EasyLog annotation;

    AnnotatedElem(VariableElement fieldElem) {
        Element parentElem = fieldElem.getEnclosingElement();
        if (parentElem.getKind() != ElementKind.CLASS) {
            throw new IllegalArgumentException("No enclosing class of " + fieldElem.getSimpleName());
        }

        this.fieldElem = fieldElem;
        this.classElement = (TypeElement) parentElem;

        annotation = fieldElem.getAnnotation(EasyLog.class);
    }

    VariableElement getFieldElem() {
        return fieldElem;
    }

    TypeElement getClassElement() {
        return classElement;
    }

    EasyLog getAnnotation() {
        return annotation;
    }
}
