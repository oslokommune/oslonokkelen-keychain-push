plugins {
    id("idea")
    id("jacoco")
    id("org.jetbrains.kotlin.jvm") version "1.5.0"
    id("com.adarshr.test-logger") version "3.0.0"
    id("com.google.protobuf") version "0.8.16"
    id("org.jlleitschuh.gradle.ktlint") version "10.0.0"
}

allprojects {
    group = "com.github.oslokommune.oslonokkelen.oslonokkelen-kpc"
    version = "1.0.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}
