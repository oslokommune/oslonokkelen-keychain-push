import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("keychain-push.kotlin-library")
    kotlin("plugin.serialization")
    application

    alias(libs.plugins.shadow)
    alias(libs.plugins.graalvm.buildtools)
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
