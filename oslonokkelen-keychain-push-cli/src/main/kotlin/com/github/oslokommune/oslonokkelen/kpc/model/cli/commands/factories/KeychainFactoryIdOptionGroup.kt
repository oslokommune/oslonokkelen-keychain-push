package com.github.oslokommune.oslonokkelen.kpc.model.cli.commands.factories

import com.github.ajalt.clikt.completion.CompletionCandidates
import com.github.ajalt.clikt.parameters.groups.OptionGroup
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required

class KeychainFactoryIdOptionGroup : OptionGroup(
    name = "Keychain factory id",
    help = "Identifies a keychain factory responsible for creating personal keychains"
) {

    val id by option(
        "--keychain-factory-id",
        help = "The profile / system you want to use",
        completionCandidates = CompletionCandidates.Custom.fromStdout("keychain-pusher auto --autocomplete KEYCHAIN_FACTORY_IDS")
    )
        .required()

}