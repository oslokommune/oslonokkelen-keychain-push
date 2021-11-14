package com.github.oslokommune.oslonokkelen.kpc.model.cli.time

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class TokenizerTest {

    @Test
    fun `Simple tokenizer`() {
        val tokenizer = Tokenizer.build<TestToken> {
            matcher("Strings", Regex("[a-zA-Z]+")) { m ->
                TestToken.Str(m.group(1))
            }
            matcher("Numbers", Regex("[0-9]+")) { m ->
                TestToken.Number(m.group(1).toInt())
            }
        }

        val tokens = tokenizer.tokenize("abc123cba321").toList()

        assertThat(tokens)
            .containsExactly(
                TestToken.Str("abc"),
                TestToken.Number(123),
                TestToken.Str("cba"),
                TestToken.Number(321)
            )
    }


    sealed interface TestToken : Tokenizer.Token {

        data class Str(val value: String) : TestToken

        data class Number(val value: Int) : TestToken

    }

}