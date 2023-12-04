package app.sixarmstudios.adventofcode2018

import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Integer.max
import java.lang.Integer.min

class Y2023D02 {
  data class Round(var red:Int = 0, var green:Int = 0, var blue:Int = 0) {
    fun total() : Int = red + green + blue
  }
  data class Game(val id:Int, val rounds:List<Round>)
  private fun buildGame(line:String):Game {
    val a = line.split(": ")
    val game = Integer.parseInt(a[0].substring("Game ".length))
    val rounds = a[1].split("; ")
      .map { s ->
        val r = Round()
        s.split(", ")
          .map { it.split(" ").let { l -> Pair(Integer.parseInt(l[0]), l[1]) } }
          .forEach { (num, color) ->
            if (color == "red") { r.red += num }
            if (color == "green") { r.green += num }
            if (color == "blue") { r.blue += num }
          }
        r
      }
    return Game(game, rounds)
  }
  private fun solvePart1(fileName: String):Int {
    val inputStream = this.javaClass.classLoader!!.getResourceAsStream(fileName)
    val isValid: (Game) -> Boolean = { g ->
      !g.rounds.any { r ->
        r.red > 12 || r.green > 13 || r.blue > 14
      }
    }
    return BufferedReader(InputStreamReader(inputStream)).readText()
      .split("\n")
      .filter { it.isNotBlank() }
      .map { buildGame(it)}
      .filter(isValid)
      .map{it.id}
      .sum()
  }
  private fun solvePart2(fileName: String):Int {
    val inputStream = this.javaClass.classLoader!!.getResourceAsStream(fileName)
    val score: (Game) -> Int = { g ->
      val mins = g.rounds.fold(Triple(0,0,0)){acc, round ->
        Triple(max(acc.first, round.red),max(acc.second, round.blue),max(acc.third, round.green))
      }
      mins.first * mins.second * mins.third
    }
    return BufferedReader(InputStreamReader(inputStream)).readText()
      .split("\n")
      .filter { it.isNotBlank() }
      .map { buildGame(it)}
      .map(score)
      .sum()
  }
  @Test
  fun testPart1Sample() {
    assertEquals(8,solvePart1("y2023_d02.sample.txt"))
  }
  @Test
  fun testPart1Mine() {
    assertEquals(2810,solvePart1("y2023_d02.mine.txt"))
  }
  @Test
  fun testPart2Sample() {
    assertEquals(2286,solvePart2("y2023_d02.sample.txt"))
  }
  @Test
  fun testPart2Mine() {
    assertEquals(6,solvePart2("y2023_d02.mine.txt"))
  }
}