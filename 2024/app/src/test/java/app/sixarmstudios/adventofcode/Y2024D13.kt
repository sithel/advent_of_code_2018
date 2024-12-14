package app.sixarmstudios.adventofcode

import junit.framework.TestCase.assertEquals
import org.junit.Test

class Y2024D13 : Shark() {

  data class Button(val x: Long, val y: Long)
  data class Prize(val x: Long, val y: Long)
  data class ClawMachine(val a: Button, val b: Button, val prize: Prize) {
    /*
    Many thanks to CodePurpose @ https://www.youtube.com/watch?v=fdhljZOFR_M
     */
    fun calcMoves():Pair<Long, Long>?{
      /*
      Button A: X+94, Y+34
      Button B: X+22, Y+67
      Prize: X=8400, Y=5400

       a.x + b.x = p.x
       a.y + b.y = p.y
       */
      val dX = prize.x * b.y - b.x * prize.y
      val dY = a.x * prize.y - prize.x * a.y
      val d = a.x*b.y- a.y * b.x
      if (d == 0L)
        return null
      val x = dX/d
      val y = dY/d
      if (x > 100 || y > 100 || x < 0 || y < 0)
        return null
      if ((a.x * x + b.x * y != prize.x) || (a.y * x + b.y * y != prize.y)) {
        println("WHAT THE FUCK!??")
        println("I see dX [$dX] - dY [$dY] - d[$d]\t\t\t\t\t\t$this")
        println(" --- solution $x, $y  -> ${x * 3 + y}")
        println("        ==>  (${a.x} * $x + ${b.x} * y = ${a.x * x} + ${b.x * y} = ${a.x * x + b.x * y} = ${prize.x})")
        println("        ==>  (${a.y} * $x + ${b.y} * y = ${a.y * x} + ${b.x * y} = ${a.y * x + b.y * y} = ${prize.y})")
        return null
      }
      return Pair(x,y)
    }
  }
  data class Arcade(val machines: List<ClawMachine>)

  val regex = Regex(
    """(?m)Button A: X\+(\d+), Y\+(\d+)\nButton B: X\+(\d+), Y\+(\d+)\nPrize: X=(\d+), Y=(\d+)""")

  fun solution1(fileName: String): Long {
    val arcade = readFile(fileName)
    println(arcade)
    return arcade.machines
      .map { it.calcMoves() }
      .filterNotNull()
      .map { (a,b) -> a*3 + b }
      .sum()
  }

  fun solution2(fileName: String): Long = 0

  fun readFile(fileName: String): Arcade {
    val matches = regex.findAll(loadFile(fileName))
    return Arcade(matches
      .map { match ->
        val g = match.groupValues
        ClawMachine(
          Button(g[1].toLong(), g[2].toLong()),
          Button(g[3].toLong(), g[4].toLong()),
          Prize(g[5].toLong(), g[6].toLong())
        )
      }
      .toList()
    )
  }

  @Test
  fun example1() {
    val fileName = "y2024_d13_example.txt"
    assertEquals(480, solution1(fileName))
  }

  @Test
  fun mine1() {
    val fileName = "y2024_d13_mine.txt"
    assertEquals(0, solution1(fileName))
    // 28923 -- too high!
    // 28032 -- too high!
  }

  @Test
  fun example2() {
    assertEquals(-1, solution2("y2024_d13_example.txt"))
  }

  @Test
  fun mine2() {
    assertEquals(-1, solution2("y2024_d13_mine.txt"))
  }
}