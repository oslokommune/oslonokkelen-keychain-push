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

/**
 * The jdk version is read from .sdkmanrc so that local shells, IntelliJ, GitHub Actions and Gradle agree on one version.
 */
val sdkmanJavaMajorVersion: Provider<Int> =
    providers
        .fileContents(layout.projectDirectory.file(".sdkmanrc"))
        .asText
        .map { content ->
            content
                .lines()
                .first { it.startsWith("java=") }
                .removePrefix("java=")
                .substringBefore(".")
                .toInt()
        }

allprojects {
    group = "com.github.oslokommune"
    version = "1.0.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    // Pin the JDK used to build so output doesn't depend on the local JAVA_HOME
    pluginManager.withPlugin("java") {
        configure<JavaPluginExtension> {
            toolchain.languageVersion.set(sdkmanJavaMajorVersion.map(JavaLanguageVersion::of))
        }
    }

    tasks.withType<JavaCompile>().configureEach {
        options.release.set(18)
    }
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_18)
        }
    }
}
