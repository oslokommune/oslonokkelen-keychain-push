/**
 * What every JVM module in the build has in common. Modules normally apply `keychain-push.kotlin-library`
 * or `keychain-push.protobuf` (which build on this one) rather than applying it directly.
 */
plugins {
    `java-library`
    idea
}

group = "com.github.oslokommune"
version = "1.0.0-SNAPSHOT"

/**
 * The jdk version is read from .sdkmanrc so that local shells, IntelliJ, GitHub Actions, JitPack and Gradle agree on one version.
 */
val sdkmanJavaMajorVersion: Provider<Int> =
    providers
        .fileContents(isolated.rootProject.projectDirectory.file(".sdkmanrc"))
        .asText
        .map { content ->
            content
                .lines()
                .first { it.startsWith("java=") }
                .removePrefix("java=")
                .substringBefore(".")
                .toInt()
        }

java {
    toolchain {
        languageVersion.set(sdkmanJavaMajorVersion.map(JavaLanguageVersion::of))
    }
}

// The published bytecode targets Java 18 regardless of which jdk builds it
tasks.withType<JavaCompile>().configureEach {
    options.release.set(18)
}
