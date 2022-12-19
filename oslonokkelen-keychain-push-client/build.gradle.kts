import com.adarshr.gradle.testlogger.TestLoggerExtension
import com.adarshr.gradle.testlogger.TestLoggerPlugin
import com.adarshr.gradle.testlogger.theme.ThemeType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    `maven-publish`
    idea

    id("org.jetbrains.kotlin.jvm")
    id("com.adarshr.test-logger")
    id("java")
}

description = "Oslonøkkelen - Keychain push client"

dependencies {
    api(project(":oslonokkelen-keychain-push-protobuf"))

    implementation("org.slf4j:slf4j-api:2.0.6")

    testImplementation("org.slf4j:slf4j-simple:2.0.6")
    testImplementation("org.assertj:assertj-core:3.23.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
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