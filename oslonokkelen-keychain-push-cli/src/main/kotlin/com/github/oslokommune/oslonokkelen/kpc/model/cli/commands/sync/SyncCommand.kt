package com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.sync

import com.charleskorn.kaml.Yaml
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.oslokommune.oslonokkelen.kpc.model.cli.cli.CliOutput
import com.github.oslokommune.oslonokkelen.kpc.model.cli.cli.CliService
import com.github.oslokommune.oslonokkelen.push.PushRequest
import com.github.oslokommune.oslonokkelen.push.TimeInterval
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate
import java.time.LocalTime

class SyncCommand(
    private val out: CliOutput,
    private val cliService: CliService
) : CliktCommand(
    help = "Sync permission",
    name = "sync"
) {

    private val file by option(
        "--file",
        help = "Yaml file containing the permission"
    ).required()

    override fun run() {
        out.debug("Pushing $file")

        val model = readModel()
        val request = PushRequest.build(model.id, model.title) {
            for (recipient in model.recipients) {
                addRecipientByPhoneNumber(recipient.countryCode, recipient.phoneNumber)
            }
            for (permission in model.permissions) {
                addPermission(
                    assetIds = permission.assetIds,
                    interval = TimeInterval(
                        start = LocalDate.parse(permission.fromDate).atTime(LocalTime.parse(permission.fromTime)),
                        end = LocalDate.parse(permission.untilDate).atTime(LocalTime.parse(permission.untilTime)),
                    )
                )
            }
            if (model.link != null) {
                externalLink(model.link.title, URI.create(model.link.uri))
            }
            if (model.information != null) {
                additionalInformation(model.information.content)
            }
        }

        cliService.withNewSession { pushClient ->
            pushClient.push(request)
        }
    }

    private fun readModel(): PermissionModel {
        return try {
            val yaml = Files.readString(Paths.get(file), Charsets.UTF_8)
            Yaml.default.decodeFromString(PermissionModel.serializer(), yaml)
        } catch (ex: Exception) {
            throw IllegalStateException("Failed to read model from $file", ex)
        }
    }
}