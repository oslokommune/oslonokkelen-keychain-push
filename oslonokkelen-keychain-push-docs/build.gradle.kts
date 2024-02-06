import org.asciidoctor.gradle.jvm.AsciidoctorTask
import java.io.InputStream
import java.util.concurrent.TimeUnit.SECONDS

plugins {
    id("org.asciidoctor.jvm.convert") version "4.0.2"
    id("org.asciidoctor.jvm.pdf") version "4.0.1"
    id("org.asciidoctor.jvm.gems") version "4.0.2"
}

repositories {
    mavenCentral()
}

tasks {
    "asciidoctor"(AsciidoctorTask::class) {

        // Changes to protobuf should invalidate previous output from this task
        inputs.dir(file("../oslonokkelen-keychain-push-protobuf/src/main/proto"))

        setOutputDir(file("../docs"))
        outputOptions {
            if (project.hasProperty("pdf")) {
                backends("pdf")
            } else {
                backends("html5")
            }
        }
        resources {
            include("images/**")
        }
        attributes(
            mapOf(
                "toc" to "left",
                "toclevels" to "3",
                "source-highlighter" to "highlightjs"
            )
        )
        setBaseDir(file("src/docs/asciidoc"))

        dependsOn("docs-verify-graphviz")
        dependsOn("docs-verify-seqdiag")
        dependsOn("docs-verify-blockdiag")
        dependsOn("docs-verify-bob")
    }
}


asciidoctorj {
    setVersion("2.5.11")
    modules.diagram.setVersion("2.2.14")
    logLevel = LogLevel.INFO
}


tasks.register<DefaultTask>("docs-verify-graphviz") {
    doFirst {
        try {
            ProcessUtil.execute("dot", "-V")
        } catch (ignored: Exception) {
            throw GradleException("Unable to find 'dot'. Please install graphviz.\nThis is required for generating documentation diagrams.\n\n\u001B[91msudo apt install graphviz\u001B[0m")
        }
    }
}

tasks.register<DefaultTask>("docs-verify-seqdiag") {
    doFirst {
        try {
            ProcessUtil.execute("seqdiag3", "--version")
        } catch (ignored: Exception) {
            throw GradleException("Unable to find 'seqdiag'. Please install seqdiag3.\nThis is required for generating documentation diagrams.\n\n\u001B[91msudo apt install python3-seqdiag\u001B[0m")
        }
    }
}

tasks.register<DefaultTask>("docs-verify-blockdiag") {
    doFirst {
        try {
            ProcessUtil.execute("blockdiag3", "--version")
        } catch (ignored: Exception) {
            throw GradleException("Unable to find 'blockdiag3 '. Please install blockdiag3.\nThis is required for generating documentation diagrams.\n\n\u001B[91msudo apt install python3-blockdiag\u001B[0m")
        }
    }
}

tasks.register<DefaultTask>("docs-verify-bob") {
    doFirst {
        try {
            ProcessUtil.execute("svgbob_cli", "--help")
        } catch (ignored: Exception) {
            throw GradleException("Unable to find 'svgbob'. Please install it.\nThis is required for generating documentation diagrams.\n\n\u001B[91mcargo install --git https://github.com/ivanceras/svgbob svgbob_cli\u001B[0m")
        }
    }
}


object ProcessUtil {

    fun execute(workingDirectory: File, vararg command: String): ProcessOutput {
        val process = ProcessBuilder(*command)
            .directory(workingDirectory)
            .start()

        return execute(process, command)
    }

    fun execute(vararg command: String): ProcessOutput {
        val process = ProcessBuilder(*command).start()

        return execute(process, command)
    }

    private fun execute(process: Process, command: Array<out String>): ProcessOutput {
        if (!process.waitFor(5, SECONDS)) {
            process.destroy()
            throw ProcessException(
                command = command.joinToString(" "),
                details = "Timed out waiting for: $command",
                exitCode = -1
            )
        }
        if (process.exitValue() != 0) {
            throw ProcessException(
                command = command.joinToString(" "),
                details = readStream(process.errorStream),
                exitCode = process.exitValue()
            )
        }

        return ProcessOutput(
            out = readStream(process.inputStream),
            err = readStream(process.errorStream)
        )
    }

    private fun readStream(stream: InputStream): String {
        return stream.reader(Charsets.UTF_8).readText().trim()
    }

}

data class ProcessOutput(
    val out: String,
    val err: String
)

class ProcessException(command: String, exitCode: Int, details: String) :
    GradleException("$command failed with exit code $exitCode\n-------------------------------\n$details")
