Oslonøkkelen - Keychain push 
============================
[![](https://jitpack.io/v/oslokommune/oslonokkelen-keychain-push.svg)](https://jitpack.io/#oslokommune/oslonokkelen-keychain-push)
[![Test](https://github.com/oslokommune/oslonokkelen-keychain-push/actions/workflows/testing.yml/badge.svg)](https://github.com/oslokommune/oslonokkelen-keychain-push/actions/workflows/testing.yml)

Users of Oslonøkkelen get access to doors via keychains. Other systems can an API to push keychains
to users who have verified their phone number. 


Docs
----
Full documentation: https://oslokommune.github.io/oslonokkelen-keychain-push/

We use Asciidoctor for our documentation. Follow these steps to submit changes. You will need some cli tools installed,
but the build tasks will tell you if any of them are missing. 

1. Update the [asciidoc files](https://github.com/oslokommune/oslonokkelen-keychain-push/tree/main/oslonokkelen-keychain-push-docs/src/docs/asciidoc)
2. Generate updated html files by running `./gradlew :oslonokkelen-keychain-push-docs:asciidoc`
3. Commit and push your changes.

Anti bikeshedding
-----------------
Please install the Ktlint git pre-commit hook to auto format changed files before 
submitting pull requests to this repository.

    ./gradlew addKtlintFormatGitPreCommitHook


Modules
-------

### :oslonokkelen-keychain-push-docs
Asciidoc documentation. Run `./gradlew :oslonokkelen-keychain-push-docs:asciidoc` to render the documentation as html 
under `docs`. It depends on having a few chart / diagram utils available on `$PATH`. The build script should detect if
any of those are missing and print instructions on how to install them. 

### :oslonokkelen-keychain-push-protobuf
Protobuf messages used in the api. Gradle tasks for compiling `.proto` to `.java`.  


### :oslonokkelen-keychain-push-client
Kotlin client.

### :oslonokkelen-keychain-push-client-ktor
Kotlin / Ktor client.
