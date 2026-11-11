plugins {
    `kotlin-dsl`
}

/**
 * Convention plugins apply third party plugins by id, so those plugins have to be on this build's classpath.
 * Turning the catalog's plugin entries into their marker artifacts keeps the versions in libs.versions.toml.
 */
fun Provider<PluginDependency>.asDependency(): Provider<String> = map { "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}" }

dependencies {
    implementation(libs.plugins.kotlin.jvm.asDependency())
    implementation(libs.plugins.kotlin.serialization.asDependency())
    implementation(libs.plugins.ktlint.asDependency())
    implementation(libs.plugins.testLogger.asDependency())
    implementation(libs.plugins.protobuf.asDependency())
}
