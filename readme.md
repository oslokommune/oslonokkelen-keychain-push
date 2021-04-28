Oslonøkkelen - Keychain push 
============================

Users of Oslonøkkelen get access to doors via keychains. Other systems can an API to push keychains
to users who have verified their phone number. 


Docs
----
Documentation is published to:
https://oslokommune.github.io/oslonokkelen-keychain-push/


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