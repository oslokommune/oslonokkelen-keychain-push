// The root project holds no source code. Shared build configuration lives in the convention plugins
// under build-logic/src/main/kotlin, which the modules apply themselves.
plugins {
    // Not applied here, but puts build-logic's classpath in the root class loader so modules share one copy of the
    // kotlin plugin and can apply the plugins it brings along (e.g. kotlin("plugin.serialization")) without a version
    id("keychain-push.kotlin-library") apply false
}
