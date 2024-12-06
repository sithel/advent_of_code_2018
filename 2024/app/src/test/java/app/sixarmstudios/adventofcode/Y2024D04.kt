package app.sixarmstudios.adventofcode

import junit.framework.TestCase.assertEquals
import org.junit.Test

class Y2024D04 : Shark() {

  private fun solution1(fileName: String): Int {
    var map = processFileData(loadFile(fileName))
      .map { it.split("").filter { it.isNotBlank() } }
    val t = "XMAS"
    val h = findHorizontal(t, map)
    val v = findVertical(t, map)
    val d = finDiagonal(t, map)
    println("h $h : v $v : d $d")
    return h + v + d
  }

  private fun findInPhrase(target: String, row: String): Int {
    var count = 0
    var start = 0
//    println("       > looking for [$target] in $row")
    while (start < row.length) {
      val found = row.indexOf(target, start, true)
//      println("         > $start < ${row.length}    -- $found")
      if (found == -1) {
        break
      }
      ++count
      start = found + target.length
    }
    return count
  }

  private fun findHorizontal(target: String, map: List<List<String>>): Int {
    var count = 0
    for (row in map) {
//      println("  ~~ [$count] $row")
      val asString = row.joinToString("")
      count += findInPhrase(target, asString)
      count += findInPhrase(target.reversed(), asString)
    }
    return count
  }

  private fun findVertical(target: String, map: List<List<String>>): Int {
//    println("c")
    var asRows = mutableListOf<String>()
    var count = 0
    for (col in 0 until map[0].size) {
      val s = map.map { it[col] }.joinToString("")
      asRows.add(s)
    }
    asRows.forEach {
      count += findInPhrase(target, it)
      count += findInPhrase(target.reversed(), it)
    }
    return count
  }

  private fun finDiagonal(target: String, map: List<List<String>>): Int {
//    println("d")
    var asRows = mutableListOf<String>()
    var count = 0
    for (col in map.size * -1 until map[0].size*2) {
      var s = ""
      for (row in 0 until map.size) {
        val entry = map.getOrNull(row)?.getOrNull(col + row)
        if (entry != null) {
          s += entry
        }
      }
      println("col $col gives : $s ")
      asRows.add(s)
      s = ""
      for (row in 0 until map.size) {
        val entry = map.getOrNull(row)?.getOrNull(map[0].size - 1 - col + row)
        if (entry != null) {
          s += entry
        }
      }
      println("col $col gives : $s ")
      asRows.add(s)
    }
//    println("diagonal entries: \n\t${asRows.joinToString("\n\t")}")
    asRows.map {
      count += findInPhrase(target, it)
      count += findInPhrase(target.reversed(), it)
      println(" >>  [$it]  --> count =  $count")
    }
    return count
  }

  /*
  ....XXMAS.      1
  .SAMXMS...      1
  ...S..A... 1
  ..A.A.MS.X
  XMASAMX.MM      2
  X.....XA.A
 1S.S.S.S.SS
  .A.A.A.A.A
  ..M.M.M.MM 1
  .X.X.XMASX      1
  1 1 2 1    1
         1 2
   */
  @Test
  fun example1() {
//    println("a")
//    assertEquals(1, findInPhrase("a","bab"))
//    assertEquals(1, findInPhrase("ab","bab"))
//    assertEquals(4, findInPhrase("ab","babababccab"))
//    assertEquals(0, findInPhrase("ab","weraasdf"))
    assertEquals(18, solution1("y2024_d04_example.txt"))
//    println("z")
  }

  @Test
  fun mine1() {
    assertEquals(1, solution1("y2024_d04_mine.txt"))
    // 2520 is too low
  }
}