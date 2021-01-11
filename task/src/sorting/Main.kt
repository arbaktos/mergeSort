package sorting

import java.io.File
import java.util.Scanner

fun main(args: Array<String>) {
    val userInput = Sort().getInput(args)
    val output = Sort().chooseOutput(userInput)

    if (userInput.hasOutputFile()) {
        File(userInput.outputFile).writeText(output)
    } else {
        println(output)
    }
}

class Sort {
    data class InputData (var dataType: String = "",
                          var inputFile: String = "",
                          var outputFile: String = "",
                          var sortingType: String = "natural",
                          var inputText: MutableList<String> = mutableListOf()) {
        fun hasOutputFile(): Boolean {
            return this.outputFile != ""
        }
        fun hasInputFile(): Boolean {
            return this.inputFile != ""
        }
        fun sortByCount(): Boolean {
            return if (this.sortingType == "byCount") true else false
        }
    }

    fun getInput(args: Array<String>): InputData {
        val userInput = InputData()
        userInput.dataType = tryAndCatch("dataType", args)
        userInput.inputFile = tryAndCatch("inputFile", args)
        userInput.outputFile = tryAndCatch("outputFile", args)
        userInput.sortingType = if (args.contains("byCount")) "byCount" else "natural"
        userInput.inputText = getInputData(userInput)
        return userInput
    }

    fun chooseOutput(userInput: InputData): String {
        return if (userInput.sortByCount()) {
            outputByCount(userInput)
        } else {
            outputNatural(userInput)
        }
    }

    private fun getInputData(userInput: InputData): MutableList<String> {
        val result = mutableListOf<String>()
        return if (userInput.hasInputFile()) {
            File(userInput.inputFile).readLines().toMutableList()
        } else {
            val scanner = Scanner(System.`in`)
            while (scanner.hasNext()) {
                when(userInput.dataType) {
                    "line" -> result.add(scanner.nextLine())
                    else -> result.add(scanner.next())
                }
            }
            result
        }
    }

    private fun tryAndCatch(param: String, args: Array<String>): String {
        return if (args.contains("-$param")) {
            try {
                args[args.indexOf("-$param") + 1]
            } catch (e: Exception) {
                println("No $param defined!")
                ""
            }
        } else {
            ""
        }
    }

    private fun outputNatural(userInput: InputData): String {
        return when (userInput.dataType) {
            "long" -> {
                val longList = userInput.inputText.map { it.toInt() }.sorted()
                "Total numbers: ${longList.size}. \n" +
                        "Sorted data: ${longList.joinToString(" ")}"
            }
            "word" -> {
                val wordList = userInput.inputText.map { it.trim() }
                "Total words: ${wordList.size}. \n" +
                        "Sorted data: ${wordList.joinToString(" ")}"
            }
            else -> {
                val lineList = userInput.inputText
                "Total lines: ${lineList.size}\n" +
                        "Sorted data:$lineList"
            }
        }
    }

    private fun outputByCount(userInput: InputData): String {
        val units: String
        val dataList = when (userInput.dataType) {
            "long" -> {
                units = "numbers"
                userInput.inputText.map { it.toInt() }.sorted()
            }
            "line" -> {
                units = "lines"
                userInput.inputText
            }
            else -> {
                units = "words"
                userInput.inputText.map { it.trim() }
            }
        }
        var output = "Total $units: ${dataList.size}"
        val countMap = dataList.groupingBy { it }.eachCount()
        countMap.toSortedMap(compareBy { it }).toList().sortedBy { (_, value) -> value }.toMap().forEach {
            output += "\n${it.key}: ${it.value} time(s) ${it.value * 100 / dataList.size}%"
        }
        return output
    }
}


