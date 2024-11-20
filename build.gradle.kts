import com.github.jengelman.gradle.plugins.shadow.transformers.Log4j2PluginsCacheFileTransformer

plugins {
    java
    idea
    id("com.gradleup.shadow") version "8.3.5"
    id("io.freefair.lombok") version "8.11"
    id("gg.jte.gradle") version "3.1.15"
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

jte {
    generate()
    binaryStaticContent = true
}

dependencies {
    implementation(platform("io.vertx:vertx-stack-depchain:4.5.11"))
    implementation(group = "io.vertx",              name = "vertx-web")
    implementation(group = "io.vertx",              name = "vertx-web-client")
    implementation(group = "io.vertx",              name = "vertx-web-templ-jte")
    implementation(group = "com.google.code.gson",  name = "gson",                 version = "2.11.0")
    implementation(group = "gg.jte",                name = "jte",                  version = "3.1.15")
    runtimeOnly   (group = "ch.qos.logback",        name = "logback-classic",      version = "1.5.12")
    implementation(group = "org.slf4j",             name = "slf4j-api",            version = "2.0.16")
    implementation(group = "org.shredzone.commons", name = "commons-suncalc",      version = "3.11")
}

tasks.shadowJar {
    dependsOn(tasks.precompileJte)

    archiveClassifier.set("")
    archiveVersion.set("")

    manifest {
        attributes["Main-Class"] = "io.vertx.core.Launcher"
        attributes["Main-Verticle"] = "me.sunstorm.weather.WeatherVerticle"
    }
}

tasks.jar {
    enabled = false
}

tasks.assemble {
    dependsOn(tasks.shadowJar)
}

