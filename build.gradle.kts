plugins {
    id("idea")
    id("jacoco")
    id("org.jetbrains.kotlin.jvm") version "1.6.10"
    id("com.adarshr.test-logger") version "3.1.0"
    id("com.google.protobuf") version "0.8.18"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
}

allprojects {
    group = "com.github.oslokommune"
    version = "1.0.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    plugins.withType<JavaPlugin> {
        java {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(16))
                vendor.set(JvmVendorSpec.ADOPTOPENJDK)
                implementation.set(JvmImplementation.J9)
            }
        }
    }
}
