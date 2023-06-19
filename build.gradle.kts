import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("idea")
    id("jacoco")
    id("org.jetbrains.kotlin.jvm") version "1.8.22"
    id("com.adarshr.test-logger") version "3.2.0"
    id("com.google.protobuf") version "0.9.3"
    id("org.jlleitschuh.gradle.ktlint") version "11.3.2"
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
    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
    }
}
