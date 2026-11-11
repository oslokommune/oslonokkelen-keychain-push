/**
 * A module that generates Java code from .proto files with protoc.
 */
plugins {
    id("keychain-push.java-base")
    id("com.google.protobuf")
}

val libs = versionCatalogs.named("libs")

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${libs.findVersion("protobuf-java").get().requiredVersion}"
    }
}
