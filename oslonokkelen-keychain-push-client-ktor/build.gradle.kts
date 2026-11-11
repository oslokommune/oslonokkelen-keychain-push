plugins {
    id("keychain-push.kotlin-library")
    id("keychain-push.publishing")
    // Also listed here so JitPack sees it and doesn't inject its own copy
    id("maven-publish")
}

description = "Oslonøkkelen - Keychain push client - Ktor"

dependencies {
    api(projects.oslonokkelenKeychainPushClient)

    implementation(platform(libs.slf4j.bom))
    implementation(libs.slf4j.api)

    api(libs.ktor.client.core)
    api(libs.ktor.client.jvm)
    api(libs.ktor.http) {
        because("Intellij doesn't seem to resolve this transitive dependency on its own")
    }

    testImplementation(libs.ktor.client.cio)
    testImplementation(libs.ktor.client.mock)

    testImplementation(libs.slf4j.simple)
}
