import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("idea")
    id("jacoco")
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.testLogger)
    alias(libs.plugins.protobuf)
    alias(libs.plugins.ktlint)
}

allprojects {
    group = "com.github.oslokommune"
    version = "1.0.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    tasks.withType<JavaCompile>().configureEach {
        sourceCompatibility = JavaVersion.VERSION_18.toString()
        targetCompatibility = JavaVersion.VERSION_18.toString()
    }
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_18)
        }
    }
}
