import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("idea")
    id("jacoco")
    id("org.jetbrains.kotlin.jvm") version "1.9.10"
    id("com.adarshr.test-logger") version "3.2.0"
    id("com.google.protobuf") version "0.9.4"
    id("org.jlleitschuh.gradle.ktlint") version "11.6.0"
}

allprojects {
    group = "com.github.oslokommune"
    version = "1.0.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    tasks.withType<JavaCompile>().configureEach {
        sourceCompatibility = JavaVersion.VERSION_17.toString()
        targetCompatibility = JavaVersion.VERSION_17.toString()
    }
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
    }
}
