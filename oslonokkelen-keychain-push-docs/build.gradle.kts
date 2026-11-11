import org.asciidoctor.gradle.jvm.AsciidoctorTask
import java.io.InputStream
import java.util.concurrent.TimeUnit.SECONDS

plugins {
    alias(libs.plugins.asciidoc.convert)
    alias(libs.plugins.asciidoc.pdf)
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

        // Diagram generation shells out to these tools, so fail early with install hints if any are missing
        doFirst {
            ProcessUtil.verifyInstalled(
                tool = "graphviz",
                command = listOf("dot", "-V"),
                installHint = "sudo apt install graphviz",
            )
            ProcessUtil.verifyInstalled(
                tool = "seqdiag3",
                command = listOf("seqdiag3", "--version"),
                installHint = "sudo apt install python3-seqdiag",
            )
            ProcessUtil.verifyInstalled(
                tool = "blockdiag3",
                command = listOf("blockdiag3", "--version"),
                installHint = "sudo apt install python3-blockdiag",
            )
            ProcessUtil.verifyInstalled(
                tool = "svgbob",
                command = listOf("svgbob_cli", "--help"),
                installHint = "cargo install --git https://github.com/ivanceras/svgbob svgbob_cli",
            )
        }
    }
}


asciidoctorj {
    setVersion(libs.versions.asciidocj)
    modules.diagram.setVersion(libs.versions.asciidoc.diagrams)
    logLevel = LogLevel.INFO
}


object ProcessUtil {

    fun verifyInstalled(tool: String, command: List<String>, installHint: String) {
        try {
            execute(*command.toTypedArray())
        } catch (ignored: Exception) {
            throw GradleException("Unable to find '${command.first()}'. Please install $tool.\nThis is required for generating documentation diagrams.\n\n\u001B[91m$installHint\u001B[0m")
        }
    }

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
