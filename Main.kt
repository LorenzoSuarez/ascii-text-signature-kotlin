package signature

import java.io.File

const val MEDIUM_PATH = "../medium.txt"
const val ROMAN_PATH = "../roman.txt"
const val SPACE = " "
const val EMPTY = ""
const val ZERO = 0

fun main() {
    print("Enter name and surname: ")
    val surname = readLine()!!.toString()
    print("Enter person's status: ")
    val status = readLine()!!.toString()

    val mediumFont: Font = getFontByFile(MEDIUM_PATH)
    val romanFont: Font = getFontByFile(ROMAN_PATH)

    val outputSurname = surname.map { letter ->
        getLetterByChar(letter = letter.toString(), romanFont, 10)
    }
    val outputStatus = status.map { letter ->
        getLetterByChar(letter = letter.toString(), mediumFont, 5)
    }
    val lengthSurname = outputSurname.sumOf { it?.get(0)?.length ?: 0 }
    val lengthStatus = outputStatus.sumOf { it?.get(0)?.length ?: 0 }

    repeat(maxOf(lengthSurname, lengthStatus) + 8) { printEight() }.also { println() }

    // Print Surname
    val repeatSurnameSpaces = ((lengthStatus + 4) - lengthSurname)
    printWord(
        romanFont,
        outputSurname,
        if (lengthSurname > lengthStatus) 2 else repeatSurnameSpaces / 2,
        if (lengthSurname > lengthStatus) 2 else repeatSurnameSpaces / 2 + if (repeatSurnameSpaces % 2 == 0) 0 else 1
    )

    // Print Status
    val repeatStatusSpaces = ((lengthSurname + 4) - lengthStatus)
    printWord(
        mediumFont,
        outputStatus,
        if (lengthStatus > lengthSurname) 2 else repeatStatusSpaces / 2,
        if (lengthStatus > lengthSurname) 2 else repeatStatusSpaces / 2 + if (repeatStatusSpaces % 2 == 0) 0 else 1
    )

    repeat(maxOf(lengthSurname, lengthStatus) + 8) { printEight() }
}

fun printEight() = print("8")
fun printEightyEight() = print("88")

fun printWord(
    font: Font,
    output: List<MutableList<String>?>,
    startIndentation: Int,
    endIndentation: Int,
) {
    font.fontSize?.run {
        repeat(this) {
            printEightyEight()
            repeat(startIndentation) { print(SPACE) }

            output.forEach { letter ->
                print("${letter?.get(it)}")
            }

            repeat(endIndentation) { print(SPACE) }
            printEightyEight()

            println()
        }
    }
}

fun getLetterByChar(letter: String, font: Font, spaces: Int): MutableList<String>? {
    return if (letter == SPACE) font.fontSize?.let {
        List(it) { " ".repeat(spaces) }.toMutableList()
    } else font
        .alphabet
        .firstOrNull { it.name == letter }?.symbols
}

private fun getFontByFile(path: String): Font {
    val font = Font()

    File(path).useLines(Charsets.ISO_8859_1) { lines ->
        var name: String = EMPTY
        var width: Int = ZERO
        val symbols: MutableList<String> = mutableListOf()

        lines.forEachIndexed { index, read ->
            with(font) {
                if (index == ZERO) {
                    read.split(SPACE).run {
                        fontSize = first().toInt()
                        numberOfCharacters = last().toInt()
                    }
                } else {
                    if (with(read) { count { it.isDigit() || it.isLetter() } == length.dec() && length in 2..4 }) read.split(SPACE).run {
                        name = first()
                        width = last().toInt()
                    } else {
                        symbols.add(read)
                        if (symbols.size == font.fontSize) {
                            alphabet.add(Letter(
                                name,
                                width,
                                symbols.toMutableList()
                            ))
                            symbols.clear()
                        }
                    }
                }
            }
        }
    }

    return font
}

data class Font(
    var fontSize: Int? = null,
    var numberOfCharacters: Int? = null,
    val alphabet: MutableList<Letter> = mutableListOf(),
)

data class Letter(
    val name: String,
    val width: Int,
    val symbols: MutableList<String> = mutableListOf(),
)

