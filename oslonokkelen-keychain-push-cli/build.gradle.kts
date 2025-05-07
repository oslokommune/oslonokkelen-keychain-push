import com.adarshr.gradle.testlogger.TestLoggerExtension
import com.adarshr.gradle.testlogger.TestLoggerPlugin
import com.adarshr.gradle.testlogger.theme.ThemeType
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `java-library`
    application
    idea

    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.shadow)
    alias(libs.plugins.graalvm.buildtools)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.testLogger)
    id("java")
}

description = "Oslonøkkelen - Keychain push client - Cli"

dependencies {
    api(projects.oslonokkelenKeychainPushClientKtor)
    implementation(platform(libs.slf4j.bom))
    implementation(libs.slf4j.api)
    implementation(libs.slf4j.nop)

    implementation(libs.clikt)
    implementation(libs.ktor.client.cio)

    implementation(libs.kaml)
    implementation(libs.kotlinx.serialization.core)

    testImplementation(libs.slf4j.simple)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation(libs.bundles.testing)
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