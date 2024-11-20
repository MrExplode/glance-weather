import com.github.jengelman.gradle.plugins.shadow.transformers.Log4j2PluginsCacheFileTransformer

plugins {
    java
    idea
    id("com.gradleup.shadow") version "8.3.5"
    id("io.freefair.lombok") version "8.11"
}

group = "me.sunstorm"
version = "1.0-SNAPSHOT"

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(group = "com.google.code.gson",     name = "gson",              version = "2.11.0")
    implementation(group = "gg.jte",                   name = "jte",               version = "3.1.15")
    implementation(group = "io.javalin",               name = "javalin",           version = "6.3.0")
    implementation(group = "io.javalin",               name = "javalin-rendering", version = "6.3.0")
    runtimeOnly   (group = "org.apache.logging.log4j", name = "log4j-core",        version = "2.24.1")
    runtimeOnly   (group = "org.apache.logging.log4j", name = "log4j-api",         version = "2.24.1")
    runtimeOnly   (group = "org.apache.logging.log4j", name = "log4j-slf4j2-impl", version = "2.24.1")
    implementation(group = "org.slf4j",                name = "slf4j-api",         version = "2.0.16")
    implementation(group = "org.shredzone.commons",    name = "commons-suncalc",   version = "3.11")
}

tasks.shadowJar {
    transform(Log4j2PluginsCacheFileTransformer())
    archiveClassifier.set("")
    archiveVersion.set("")

    manifest {
        attributes["Main-Class"] = "me.sunstorm.weather.Bootstrap"
    }
}

tasks.jar {
    enabled = false
}

tasks.assemble {
    dependsOn(tasks.shadowJar)
}

