import com.adarshr.gradle.testlogger.theme.ThemeType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

/**
 * A module with handwritten Kotlin: linted and tested with JUnit + AssertJ.
 */
plugins {
    id("keychain-push.java-base")
    id("org.jetbrains.kotlin.jvm")
    id("org.jlleitschuh.gradle.ktlint")
    id("com.adarshr.test-logger")
}

val libs = versionCatalogs.named("libs")

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

dependencies {
    testImplementation(libs.findBundle("testing").get())
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
    reports {
        html.required.set(true)
        junitXml.required.set(true)
    }
}

testlogger {
    theme = ThemeType.MOCHA_PARALLEL
    slowThreshold = 5000
    showStackTraces = true
    showCauses = true
}
