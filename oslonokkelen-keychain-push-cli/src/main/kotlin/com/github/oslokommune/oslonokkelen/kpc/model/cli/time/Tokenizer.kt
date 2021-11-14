package com.github.oslokommune.oslonokkelen.kpc.model.cli.time

import java.util.regex.Matcher
import java.util.regex.Pattern

class Tokenizer<T : Tokenizer.Token> private constructor(private val matchers: List<RegexMatcher<T>>) {

    private val emptyPattern = Pattern.compile("")

    fun tokenize(input: String) : Sequence<T> {
        return sequence {
            val m = emptyPattern.matcher(input)

            while (!m.hitEnd()) {
                val matchingMatcher = matchers.firstOrNull { parser ->
                    parser(m)?.also {
                        yield(it)
                    } != null
                }

                if (matchingMatcher == null && !m.hitEnd()) {
                    throw IllegalStateException("Parse exception")
                }
            }
        }
    }

    companion object {

        fun <T : Token> build(builder: BuilderCtx<T>.() -> Unit): Tokenizer<T> {
            val ctx = BuilderCtx<T>()
            builder(ctx)

            return Tokenizer(ctx.matchers)
        }

    }


    class BuilderCtx<T : Token> internal constructor() {

        internal val matchers = mutableListOf<RegexMatcher<T>>()

        fun matcher(name: String, regex: Regex, tokenFactory: (Matcher) -> T) {
            val redefinedPattern = Pattern.compile("^(${regex.pattern})")
            val matcher = RegexMatcher(name, redefinedPattern, tokenFactory)

            matchers.add(matcher)
        }

    }

    internal class RegexMatcher<T : Token>(
        private val name: String,
        private val pattern: Pattern,
        private val tokenFactory: (Matcher) -> T?
    ) {

        operator fun invoke(matcher: Matcher) : T? {
            matcher.usePattern(pattern)

            return if (matcher.lookingAt()) {
                try {
                    tokenFactory(matcher).also {
                        matcher.region(matcher.end(), matcher.regionEnd())
                    }
                } catch (ex: Exception) {
                    throw IllegalStateException("Exception in $name", ex)
                }
            } else {
                null
            }
        }

    }

    interface Token

}