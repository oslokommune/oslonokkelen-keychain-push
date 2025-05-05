import com.adarshr.gradle.testlogger.TestLoggerExtension
import com.adarshr.gradle.testlogger.TestLoggerPlugin
import com.adarshr.gradle.testlogger.theme.ThemeType

plugins {
    `java-library`
    `maven-publish`
    idea

    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.testLogger)
    id("java")
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
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation(libs.bundles.testing)
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

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
