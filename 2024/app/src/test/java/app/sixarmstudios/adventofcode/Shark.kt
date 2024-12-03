package app.sixarmstudios.adventofcode

import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.text.split

open class Shark {
  fun loadFile(fileName: String): String {
    val inputStream = this.javaClass.classLoader!!.getResourceAsStream(fileName)
    return BufferedReader(InputStreamReader(inputStream)).readText()
  }

  fun processFileData(input: String): List<String> {
    return input.split("\n")
      .map { it.trim() }
      .filter { it.isNotBlank() }
  }

}