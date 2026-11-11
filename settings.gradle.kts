plugins {
    // Lets Gradle download the JDK toolchain when it isn't installed locally (e.g. on JitPack)
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "oslonokkelen-keychain-push"

include(":oslonokkelen-keychain-push-protobuf")
include(":oslonokkelen-keychain-push-client")
include(":oslonokkelen-keychain-push-client-ktor")
include(":oslonokkelen-keychain-push-docs")
include(":oslonokkelen-keychain-push-cli")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
