package com.codigine.easylogplugin

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.jetbrains.kotlin.gradle.plugin.KotlinAndroidPluginWrapper

public class EasyLogPlugin implements Plugin<Project> {
    def GEN_SRC_SUB_DIR = "/generated/source/easylog"
    def CONFIG_CLASS_NAME = "EasyLogConfig"
    def CONFIG_FILE_NAME = "${CONFIG_CLASS_NAME}.java"

    private def getConfigFile(Project project) {
        new File("$project.buildDir$GEN_SRC_SUB_DIR", CONFIG_FILE_NAME)
    }

    private def getKotlinClassesDirPath(Project project, String variantName) {
        "$project.buildDir/tmp/kotlin-classes/$variantName"
    }

    @Override
    void apply(Project project) {
        def hasApp = project.plugins.withType(AppPlugin)
        def hasLib = project.plugins.withType(LibraryPlugin)
        def hasKotlin = project.plugins.withType(KotlinAndroidPluginWrapper)
        if (!hasApp && !hasLib) {
            throw new IllegalStateException("'android' or 'android-library' plugin required.")
        }

        def extension = project.extensions.create("easylog", PluginConfig)

        def log = project.logger

        final def variants
        if (hasApp) {
            variants = project.android.applicationVariants
        } else {
            variants = project.android.libraryVariants
        }

        def pluginVersion = EasyLogPlugin.class.getPackage().getImplementationVersion()

        project.dependencies.add('compile', 'org.aspectj:aspectjrt:1.8.9')
        project.dependencies.add('compile', "com.codigine:easylog:$pluginVersion")
        if (hasKotlin) {
            project.dependencies.add('kapt', "com.codigine.easylog:annotation-processor:$pluginVersion")
        } else {
            project.dependencies.add('annotationProcessor', "com.codigine.easylog:annotation-processor:$pluginVersion")
        }

        variants.all { variant ->

            JavaCompile javaCompile = variant.javaCompile
            javaCompile.source += getConfigFile(project).parentFile
            def capitalizedVariant = variant.name.capitalize()
            def ajcTaskName = 'ajc' + capitalizedVariant

            project.task(ajcTaskName) {
                doLast {
                    log.info("Ajc task")
                    String[] args = [
                            "-showWeaveInfo",
                            "-1.5",
                            "-verbose",
                            "-inpath", javaCompile.destinationDir.toString(),
                            "-aspectpath", javaCompile.classpath.asPath,
                            "-d", javaCompile.destinationDir.toString(),
                            "-classpath", javaCompile.classpath.asPath,
                            "-bootclasspath", project.android.bootClasspath.join(
                            File.pathSeparator)
                    ]

                    log.info("Variant: " + variant.name)
                    log.info("Project build dir: " + project.buildDir)
                    log.info("Ajc args: " + Arrays.toString(args))
                    log.info("Extension: " + extension)
                    log.info("javaCompile.classpath.asPath: " + javaCompile.classpath.asPath)
                    log.info("javaCompile.destinationDir: " + javaCompile.destinationDir.toString())

                    log.info("Has Kotlin: " + (hasKotlin ? true : false))
                    log.info("Kotlin classes: " + getKotlinClassesDirPath(project, variant.name))
                    log.info("Source sets: " + project.android.sourceSets.main.java.srcDirs)

                    log.info("pluginVersion: " +EasyLogPlugin.class.getPackage().getImplementationVersion())

                    MessageHandler handler = new MessageHandler(true);
                    new Main().run(args, handler)

                    for (IMessage message : handler.getMessages(null, true)) {
                        switch (message.getKind()) {
                            case IMessage.ABORT:
                            case IMessage.ERROR:
                            case IMessage.FAIL:
                                log.error message.message, message.thrown
                                break
                            case IMessage.WARNING:
                            case IMessage.INFO:
                                log.info message.message, message.thrown
                                break
                            case IMessage.DEBUG:
                                log.debug message.message, message.thrown
                                break
                        }
                    }

                    if(hasKotlin) {
                        log.info("Kotlin weaving")

                        String[] argsKotlin = [
                                "-showWeaveInfo",
                                "-1.5",
                                "-verbose",
                                "-inpath", getKotlinClassesDirPath(project, variant.name),
                                //Only where java aspect are placed:
                                "-aspectpath",javaCompile.destinationDir.toString(),
                                "-d", getKotlinClassesDirPath(project, variant.name),
                                //To see java classes:
                                "-classpath", javaCompile.classpath.asPath + ":" + javaCompile.destinationDir.toString(),
                                "-bootclasspath", project.android.bootClasspath.join(
                                File.pathSeparator)
                        ]

                        log.info("Kotlin ajc args: " + Arrays.toString(argsKotlin))

                        MessageHandler handler1 = new MessageHandler(true);
                        new Main().run(argsKotlin, handler1)

                        for (IMessage message : handler1.getMessages(null, true)) {
                            switch (message.getKind()) {
                                case IMessage.ABORT:
                                case IMessage.ERROR:
                                case IMessage.FAIL:
                                    log.error message.message, message.thrown
                                    break
                                case IMessage.WARNING:
                                case IMessage.INFO:
                                    log.info message.message, message.thrown
                                    break
                                case IMessage.DEBUG:
                                    log.debug message.message, message.thrown
                                    break
                            }
                        }
                    }
                }

                mustRunAfter "compile" + capitalizedVariant + "JavaWithJavac"
            }
            project.tasks['compile' + capitalizedVariant + 'Ndk'].dependsOn(ajcTaskName)

            javaCompile.doFirst {
                def propertyFile = getConfigFile(project)
                propertyFile.getParentFile().mkdirs()
                propertyFile.createNewFile()
                def text = """package com.codigine.easylog;
                    |public class $CONFIG_CLASS_NAME implements LoggingConfig {
                    |
                    |    public boolean isGlobalFileLoggingEnabled() {return $extension.fileLoggingEnabled;}
                    |
                    |    public String getCommonLoggingDirectory() {return \"$extension.commonLoggingDirectory\";}
                    |
                    |    public String getCommonLoggingFile() {return \"$extension.commonLoggingFile\";}
                    |
                    |    public boolean isGlobalLogcatEnabled() {return $extension.logcatEnabled;}
                    |
                    |    public int getMaxLogFileSize() {return $extension.maxFileLogSize;}
                    |
                    |    public java.text.SimpleDateFormat getDateFormat() {return new java.text.SimpleDateFormat(\"$extension.dateFormat\");}
                    |}
                    |""".stripMargin()

                propertyFile.withWriter('UTF-8') {
                    it.write(text)
                }
            }
        }
    }
}
