package com.codigine.easylog.annotation_processor

import com.google.testing.compile.Compilation
import com.google.testing.compile.JavaFileObjects
import org.junit.Assert
import org.junit.Test

import static com.google.testing.compile.Compiler.javac

class AspectGeneratorTest extends GroovyTestCase {

    @Test
    void testProcessingDefaults() {
        runTest("Annotated.java", "ExpectedForAnnotated.java")
    }

    @Test
    void testProcessingAllAnnotationParams() {
        runTest("FullAnnotated.java", "ExpectedForFullAnnotated.java")
    }

    @Test
    void testProcessingInnerClass() {
        runTest("AnnotatedInner.java", "ExpectedForAnnotatedInner.java")
    }

    @Test
    void testProcessingAnonymousClass() {
        def compilation = newCompilation("AnnotatedAnonymous.java")
        Assert.assertEquals(Compilation.Status.SUCCESS, compilation.status())
        Assert.assertEquals(0, compilation.generatedSourceFiles().size())
    }

    @Test
    void testFailedLocalVariableAnnotation() {
        def compilation = newCompilation("AnnotatedLocal.java")
        Assert.assertEquals(Compilation.Status.FAILURE, compilation.status())
        Assert.assertEquals(1, compilation.errors().size())
    }

    private def runTest(String testedFileName, String expectedFileName) {
        def compilation = newCompilation(testedFileName)

        println "Errors: ${compilation.errors()}"

        Assert.assertEquals(Compilation.Status.SUCCESS, compilation.status())

        def generated = readFull(compilation.generatedSourceFiles().get(0).openInputStream())
        println "Generated source: \n\n---------------\n$generated\n---------------\n"

        def expected = readFull(getClass().getClassLoader().getResourceAsStream(expectedFileName))
        println "Expected source: \n\n---------------\n$expected\n---------------\n"
        Assert.assertEquals("Generated source incorrect", expected, generated)
    }

    private static def newCompilation(String source) {
        javac().withProcessors(new EasyLogAnnotationProcessor()).compile(JavaFileObjects.forResource(source))
    }


    private static String readFull(InputStream inputStream) {
        Scanner s = new Scanner(inputStream).useDelimiter("\\A")
        return s.hasNext() ? s.next() : ""
    }

}
