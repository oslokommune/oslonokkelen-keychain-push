package com.github.oslokommune.oslonokkelen.kpc.model

object Id {

    private val pattern = Regex("^[a-z0-9\\-_]{1,40}$")

    fun validate(input: String) {
        if (!input.matches(pattern)) {
            throw IllegalArgumentException("'$input' is not a valid id")
        }
    }

}