import com.adarshr.gradle.testlogger.TestLoggerExtension
import com.adarshr.gradle.testlogger.TestLoggerPlugin
import com.adarshr.gradle.testlogger.theme.ThemeType

plugins {
    `java-library`
    `maven-publish`
    idea

    id("org.jetbrains.kotlin.jvm")
    id("com.adarshr.test-logger")
    id("java")
}

description = "Oslonøkkelen - Keychain push client - Ktor"

val ktorVersion = "2.2.2"
val slf4jVersion = "2.0.6"

dependencies {
    api(project(":oslonokkelen-keychain-push-client"))

    implementation("org.slf4j:slf4j-api:$slf4jVersion")

    api("io.ktor:ktor-client-core:$ktorVersion")
    api("io.ktor:ktor-client-core-jvm:$ktorVersion")
    api("io.ktor:ktor-http:$ktorVersion") {
        because("Intellij doesn't seem to resolve this transitive dependency on its own")
    }

    testImplementation("io.ktor:ktor-client-cio:$ktorVersion")
    testImplementation("io.ktor:ktor-client-mock:$ktorVersion")

    testImplementation("org.slf4j:slf4j-simple:$slf4jVersion")
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
