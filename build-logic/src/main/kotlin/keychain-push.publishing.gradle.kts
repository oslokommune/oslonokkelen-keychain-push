/**
 * A module published as a library through JitPack.
 *
 * JitPack injects maven-publish into build scripts that don't mention it, so modules applying this plugin
 * should also list id("maven-publish") in their own plugins block.
 */
plugins {
    id("keychain-push.java-base")
    `maven-publish`
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
