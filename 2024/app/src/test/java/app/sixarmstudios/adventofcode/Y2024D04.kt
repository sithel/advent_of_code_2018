package app.sixarmstudios.adventofcode

import junit.framework.TestCase.assertEquals
import org.junit.Test

class Y2024D04 : Shark() {

  private fun solution1(fileName: String): Int {
    var map = processFileData(loadFile(fileName))
      .map { it.split("").filter { it.isNotBlank() } }
    val t = "XMAS"
    return findHorizontal(t, map) + findVertical(t, map) + finDiagonal(t, map)
  }

  private fun findInPhrase(target: String, row: String): Int {
    var count = 0
    var start = 0
    while (start < row.length) {
      val found = row.indexOf(target, start, true)
      if (found == -1) {
        break
      }
      ++count
      start = found
    }
    return count
  }

  private fun findHorizontal(target: String, map: List<List<String>>): Int {
    var count = 0
    for (row in map) {
      val asString = row.joinToString("")
      count += findInPhrase(target, asString)
      count += findInPhrase(target.reversed(), asString)
    }
    return count
  }

  private fun findVertical(target: String, map: List<List<String>>): Int {
    var asRows = mutableListOf<String>()
    var count = 0
    for (col in 0 until map[0].size) {
      val s = map.map { it[col] }.joinToString("")
      asRows.add(s)
    }
    asRows.map {
      count += findInPhrase(target, it)
      count += findInPhrase(target.reversed(), it)
    }
    return count
  }

  private fun finDiagonal(target: String, map: List<List<String>>): Int {
    var asRows = mutableListOf<String>()
    var count = 0
    for (col in 0 until map[0].size) {
      val s = map.map { it[col] }.joinToString("")
      for (row in 0 until map.size) {
        asRows.add(s)
      }
    }
    asRows.map {
      count += findInPhrase(target, it)
      count += findInPhrase(target.reversed(), it)
    }
    return count
  }

  @Test
  fun example1() {
    assertEquals(18, solution1("y2024_d04_example.txt"))
  }

  @Test
  fun mine1() {
    assertEquals(1, solution1("y2024_d04_mine.txt"))
  }
}