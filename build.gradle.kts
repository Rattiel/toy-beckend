plugins {
    java
    kotlin("jvm")
}

fun toolchainVersion(): Int {
    if (project.hasProperty("testToolchain")) {
        return project.property("testToolchain").toString().toInt()
    }
    return 21
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    group = "com.example"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(toolchainVersion())
        }
    }

    kotlin {
        jvmToolchain {
            languageVersion = JavaLanguageVersion.of(toolchainVersion())
        }
        compilerOptions {
            freeCompilerArgs.addAll("-Xjsr305=strict")
        }
    }
}