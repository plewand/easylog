package com.codigine.easylog.annotation_processor;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;

import javax.annotation.processing.Filer;
import javax.lang.model.util.Elements;

class GeneratedCodeWriter {
    static void save(Filer filer, Elements elements, AnnotatedElem annotatedElem, TypeSpec typeSpec) throws IOException {
        JavaFile.builder(elements.getPackageOf(annotatedElem.getClassElement()) + ".generated", typeSpec)
                .build()
                .writeTo(filer);
    }
}
