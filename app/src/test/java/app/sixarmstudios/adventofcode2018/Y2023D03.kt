package app.sixarmstudios.adventofcode2018

import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.io.BufferedReader
import java.io.InputStreamReader

class Y2023D03 {
  private fun isNum(v:String) : Boolean{
    try {
      Integer.parseInt(v)
      return true
    } catch (e:Exception) {
      return false
    }
  }

  private fun isSymbol(v: String?): Boolean {
    if (v == null) return false
    return !(v == "." || isNum(v))
  }
  private fun solvePart1(fileName: String):Int {
    val inputStream = this.javaClass.classLoader!!.getResourceAsStream(fileName)
    var result = 0
    val data = BufferedReader(InputStreamReader(inputStream)).readText()
      .split("\n")
      .filter { it.isNotBlank() }
      .map {l-> l.split("").filter { it.isNotBlank() } }
    data.forEachIndexed { r, line ->
      var running = false
      var curValid = false
      var total = 0
      line.forEachIndexed { c, v ->
        val isNum = isNum(v)
        if(isNum && !running){
          running = true
          total = Integer.parseInt(v)
        } else if (isNum && running) {
          total = total * 10 + Integer.parseInt(v)
        }
        if (isNum && running && !curValid) { curValid = searchForValid(data, r, c)}
        if ((!isNum || line.getOrNull(c+1) == null) && running) {
          running = false
          if (curValid) result += total
          println("Stopping @ $r,$c [$v] --> $total ($curValid) --->> $result")
          total = 0
          curValid = false
        }
      }
    }
    return result
  }

  private fun solvePart2(fileName: String):Int {
    val gearMap = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>>()
    val inputStream = this.javaClass.classLoader!!.getResourceAsStream(fileName)
    val data = BufferedReader(InputStreamReader(inputStream)).readText()
      .split("\n")
      .filter { it.isNotBlank() }
      .map {l-> l.split("").filter { it.isNotBlank() } }
    data.forEachIndexed { r, line ->
      var running = false
      var total = 0
      var gear:Pair<Int, Int>? = null
      line.forEachIndexed { c, v ->
        val isNum = isNum(v)
        if(isNum && !running){
          running = true
          total = Integer.parseInt(v)
        } else if (isNum && running) {
          total = total * 10 + Integer.parseInt(v)
        }
        if (isNum) {
          val opt = searchForValid2(data, r, c)
          if (opt != null) {
            gear = opt
            println("I see gear pivot $gear")
          }
        }
        if ((!isNum || line.getOrNull(c+1) == null) && running) {
          running = false
          gear?.let {g ->
            val entry =gearMap[g]
            println("looking up pivot $g w/ $total --> $entry")
            if (entry != null) {
              gearMap[g] = Pair(entry.first, total)
            } else {
              gearMap[g] = Pair(total, 0)
            }
          }
          gear = null
          total = 0
        }
      }
    }
    println("At the end I see gearMap : $gearMap")
    return gearMap.values.fold(0) {acc, p ->
      acc + (p.first * p.second)
    }
  }

  private fun searchForValid2(data: List<List<String>>, r: Int, c: Int): Pair<Int, Int>? {
    listOf(-1, 0, 1).any { rm ->
      listOf(-1, 0, 1).any { cm ->
        val y = data.getOrNull(r + rm)?.getOrNull(c + cm)
        if (y == "*")
          return Pair(r + rm, c + cm)
        false
      }
    }
    return null
  }

  private fun searchForValid(data: List<List<String>>, r: Int, c: Int): Boolean {
    return listOf(-1,0,1).any { rm ->
      listOf(-1,0,1).any {cm ->
        val y = data.getOrNull(r + rm)?.getOrNull(c + cm)
        val x = isSymbol(y)
        if (x) println("      - [${r + rm}][${c + cm}] '$y' -> $x")
        x
      }
    }
  }


  @Test
  fun part1_sample(){
    assertEquals(4361, solvePart1("y2023_d03.sample.txt"))
  }
  @Test
  fun part1_mine(){
//    assertEquals(0, solvePart1("y2023_d03.mine.txt"))// 537762 - too high
//    assertEquals(0, solvePart1("y2023_d03.mine.txt"))// 540575 -That's not the right answer; your answer is too high.
    assertEquals(0, solvePart1("y2023_d03.mine.txt"))// 535235 - correct
  }
  @Test
  fun part2_sample(){
    assertEquals(467835, solvePart2("y2023_d03.sample.txt"))
  }
  @Test
  fun part2_mine(){
    assertEquals(0, solvePart2("y2023_d03.mine.txt"))
  }
}