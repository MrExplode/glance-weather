plugins {
    java
    idea
    application
    id("io.freefair.lombok") version "8.11"
    id("gg.jte.gradle") version "3.1.15"
    id("org.graalvm.buildtools.native") version "0.10.4"
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

application {
    mainClass = "me.sunstorm.weather.Bootstrap"
}

repositories {
    mavenCentral()
}

jte {
    generate()
    binaryStaticContent = true
    jteExtension("gg.jte.nativeimage.NativeResourcesExtension")
}

dependencies {
    implementation(platform("io.vertx:vertx-stack-depchain:4.5.11"))
    implementation(group = "io.vertx",              name = "vertx-web")
    implementation(group = "io.vertx",              name = "vertx-web-client")
    implementation(group = "io.vertx",              name = "vertx-web-templ-jte")
    implementation(group = "com.google.code.gson",  name = "gson",                 version = "2.11.0")
    implementation(group = "gg.jte",                name = "jte",                  version = "3.1.15")
    jteGenerate   (group = "gg.jte",                name = "jte-native-resources", version = "3.1.15")
    runtimeOnly   (group = "ch.qos.logback",        name = "logback-classic",      version = "1.5.16")
    implementation(group = "org.slf4j",             name = "slf4j-api",            version = "2.0.16")
    implementation(group = "org.shredzone.commons", name = "commons-suncalc",      version = "3.11")
}

graalvmNative {
    toolchainDetection = false

    agent {
        defaultMode = "standard"
        enabled = true

        metadataCopy {
            inputTaskNames.add("run")
            outputDirectories.add("src/main/resources/META-INF/native-image/me.sunstorm/weather-extension")
        }
    }

    binaries {
        named("main") {
            imageName.set("weather-extension")
            mainClass.set("me.sunstorm.weather.Bootstrap")
            buildArgs.add("--enable-url-protocols=https")
        }
    }
}

tasks.nativeCompile {
    dependsOn(tasks.precompileJte)
}

