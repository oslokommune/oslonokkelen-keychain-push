import com.adarshr.gradle.testlogger.TestLoggerExtension
import com.adarshr.gradle.testlogger.TestLoggerPlugin
import com.adarshr.gradle.testlogger.theme.ThemeType
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `java-library`
    application
    idea

    kotlin("plugin.serialization") version "1.9.23"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("org.graalvm.buildtools.native") version "0.10.1"
    id("org.jetbrains.kotlin.jvm")
    id("com.adarshr.test-logger")
    id("java")
}

description = "Oslonøkkelen - Keychain push client - Cli"

val slf4jVersion = "2.0.12"

dependencies {
    api(project(":oslonokkelen-keychain-push-client-ktor"))
    implementation("com.github.ajalt.clikt:clikt-jvm:4.2.2")
    implementation("org.slf4j:slf4j-api:$slf4jVersion")
    implementation("org.slf4j:slf4j-nop:$slf4jVersion")
    implementation("io.ktor:ktor-client-cio:2.3.8")

    implementation("com.charleskorn.kaml:kaml:0.57.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.6.3")

    testImplementation("org.slf4j:slf4j-simple:$slf4jVersion")
    testImplementation("org.assertj:assertj-core:3.25.3")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

application {
    mainClass.set("com.github.oslokommune.oslonokkelen.kpc.model.cli.KeychainPushCliKt")
}

graalvmNative {
    binaries {
        getByName("main") {
            imageName.set("keychain-pusher")
            fallback.set(false)
            debug.set(false)
            sharedLibrary.set(false)
            buildArgs.add("--initialize-at-build-time=io.ktor,kotlinx,kotlin")
        }
    }
}

plugins.withType<TestLoggerPlugin> {
    configure<TestLoggerExtension> {
        theme = ThemeType.MOCHA_PARALLEL
        slowThreshold = 5000
        showStackTraces = true
        showCauses = true
    }
}

tasks.test {
    useJUnitPlatform()
    reports {
        html.required.set(true)
        junitXml.required.set(true)
    }
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("keychain-pusher")
        archiveClassifier.set("")
        archiveVersion.set("")

        mergeServiceFiles()
        manifest {
            attributes(mapOf("Main-Class" to "com.github.oslokommune.oslonokkelen.kpc.model.cli.KeychainPushCliKt"))
        }
    }
}