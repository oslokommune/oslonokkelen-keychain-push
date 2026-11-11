plugins {
    id("keychain-push.protobuf")
    id("keychain-push.publishing")
    // Also listed here so JitPack sees it and doesn't inject its own copy
    id("maven-publish")
}

project.description = "Defines protobuf messages used in api"

dependencies {
    api(libs.protobuf.java)
}

protobuf {
    generateProtoTasks {
        ofSourceSet("protobuf").forEach { task ->
            task.descriptorSetOptions.includeImports = true
            task.generateDescriptorSet = true
        }
    }
}
