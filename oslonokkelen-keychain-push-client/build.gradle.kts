plugins {
    id("keychain-push.kotlin-library")
    id("keychain-push.publishing")
    // Also listed here so JitPack sees it and doesn't inject its own copy
    id("maven-publish")
}

description = "Oslonøkkelen - Keychain push client"

dependencies {
    api(projects.oslonokkelenKeychainPushProtobuf)
    api(libs.kotlinx.datetime)

    implementation(platform(libs.slf4j.bom))
    implementation(libs.slf4j.api)

    testImplementation(libs.slf4j.simple)
}
