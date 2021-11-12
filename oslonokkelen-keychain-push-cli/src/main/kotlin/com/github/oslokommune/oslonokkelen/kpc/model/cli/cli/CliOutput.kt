package com.github.oslokommune.oslonokkelen.kpc.model.cli.cli

class CliOutput {

    fun print(str: String) {
        System.err.println(str)
    }

    fun warn(str: String) {
        System.err.println("Warning: $str")
    }

    fun keyValues(block: KeyValuesDsl.() -> Unit) {
        val dsl = KeyValuesDsl()
        block(dsl)
        dsl.print()
    }

    class KeyValuesDsl {

        private var keyWidth = 0
        private val entries = mutableListOf<Pair<String, String>>()

        fun add(key: String, value: String) {
            val formattedKey = "$key:"

            if (formattedKey.length > keyWidth) {
                keyWidth = formattedKey.length
            }

            entries.add(formattedKey to value)
        }

        fun print() {
            for (entry in entries) {
                print(entry.first.padEnd(keyWidth + 1, ' '))
                println(entry.second)
            }
        }
    }

    fun table(caption: String? = null, block: TableDsl.() -> Unit) {
        val table = TableDsl()
        block(table)

        if (caption != null) {
            println("")
            println(caption.uppercase())
        }
        table.print()
    }

    fun header(text: String, level: Int = 1) {
        println("")
        println(text)

        val separator = when (level) {
            1 -> "="
            2 -> "-"
            else -> "~"
        }

        println(separator.repeat(text.length))
    }

    fun debug(message: String) {
        println(message)
    }

    class TableDsl {

        private val headers = mutableListOf<String>()
        private val columnPaddings = mutableListOf<Int>()
        private val rows = mutableListOf<List<String>>()

        fun headers(vararg names: String) {
            headers.clear()

            for (name in names) {
                columnPaddings.add(name.length)
                headers.add(name)
            }
        }

        fun row(vararg values: String) {
            rows.add(values.toList())

            values.forEachIndexed { index, value ->
                if (value.length > columnPaddings[index]) {
                    columnPaddings[index] = value.length
                }
            }
        }

        fun print() {
            printSeparator()
            printRow(headers)
            printSeparator()

            for (row in rows) {
                printRow(row)
            }

            printSeparator()
        }

        private fun printRow(cellValues: List<String>) {
            print("|")
            cellValues.forEachIndexed { index, header ->
                val width = columnPaddings[index] + 1
                print(" ")
                print(header.padEnd(width, ' '))

                if (index < columnPaddings.size - 1) {
                    print("|")
                } else {
                    print("|\n")
                }
            }
        }

        private fun printSeparator() {
            for (columnPadding in columnPaddings) {
                print("+")
                print("-".repeat(columnPadding + 2))
            }
            print("+\n")
        }
    }
}
