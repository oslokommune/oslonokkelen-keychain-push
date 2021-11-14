package com.github.oslokommune.oslonokkelen.kpc.model.cli.time

import java.lang.UnsupportedOperationException
import java.time.Duration
import java.time.LocalDateTime

class DateTimeParser private constructor(private val parseRelativeTo: LocalDateTime) {

    fun parse(input: String): LocalDateTime {
        var parser: Parser = EmptyParser()

        for (token in tokenizer.tokenize(input)) {
            parser = parser.on(token)
        }

        return if (parser is CompletedParser) {
            parser.result
        } else {
            throw UnsupportedOperationException("Failed to parse: $input")
        }
    }

    companion object {

        private val tokenizer = Tokenizer.build<TimeToken> {
            matcher("Keywords", Regex("now")) {
                TimeToken.KeywordToken(TimeToken.KeywordToken.Keyword.NOW)
            }
            matcher("Time offset", Regex("([+\\-])(\\d+)([mhd])")) { matcher ->
                val operator = matcher.group(2)
                val number = matcher.group(3).toLong()

                val amount = if (operator == "+") {
                    number
                } else {
                    number * -1
                }

                when (matcher.group(4)) {
                    "m" -> TimeToken.OffsetToken(Duration.ofMinutes(amount))
                    "h" -> TimeToken.OffsetToken(Duration.ofHours(amount))
                    "d" -> TimeToken.OffsetToken(Duration.ofDays(amount))
                    else -> throw UnsupportedOperationException("Unknown time unit: ${matcher.group(3)}")
                }
            }
            matcher("Local date and time", Regex("(\\d{4})-(\\d{2})-(\\d{2})T(\\d{2}):(\\d{2}):(\\d{2})")) { matcher ->
                TimeToken.LocalDateAndTime(LocalDateTime.parse(matcher.group(0)))
            }
        }

        fun newParser(rightNow: String): DateTimeParser {
            return newParser(LocalDateTime.parse(rightNow))
        }

        fun newParser(rightNow: LocalDateTime): DateTimeParser {
            return DateTimeParser(rightNow)
        }
    }

    private sealed interface TimeToken : Tokenizer.Token {

        data class KeywordToken(val keyword: Keyword) : TimeToken {
            enum class Keyword {
                NOW
            }
        }

        data class LocalDateAndTime(val time: LocalDateTime) : TimeToken {

        }

        data class OffsetToken(val duration: Duration) : TimeToken

    }

    private interface Parser {

        fun on(token: TimeToken): Parser

    }

    private inner class EmptyParser : Parser {
        override fun on(token: TimeToken): Parser {
            return when (token) {
                is TimeToken.KeywordToken -> {
                    when (token.keyword) {
                        TimeToken.KeywordToken.Keyword.NOW -> {
                            CompletedParser(parseRelativeTo)
                        }
                    }
                }
                is TimeToken.LocalDateAndTime -> {
                    CompletedParser(token.time)
                }
                is TimeToken.OffsetToken -> {
                    throw UnsupportedOperationException("Unexpected offset, try something like: now+2h")
                }
            }
        }
    }

    private class CompletedParser(val result: LocalDateTime) : Parser {
        override fun on(token: TimeToken): Parser {
            return when (token) {
                is TimeToken.OffsetToken -> {
                    CompletedParser(result = result + token.duration)
                }
                else -> {
                    throw UnsupportedOperationException("Unexpected token: $token")
                }
            }
        }
    }

}