plugins {
    id("idea")
    id("jacoco")
    id("org.jetbrains.kotlin.jvm") version "1.5.21"
    id("com.adarshr.test-logger") version "3.0.0"
    id("com.google.protobuf") version "0.8.17"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
}

allprojects {
    group = "com.github.oslokommune"
    version = "1.0.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}
