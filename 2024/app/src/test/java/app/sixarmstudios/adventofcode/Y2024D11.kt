package app.sixarmstudios.adventofcode

import junit.framework.TestCase.assertEquals
import org.junit.Test

class Y2024D11 : Shark() {

  fun solution1(input: String, blinkCount: Long): Int {
    val stones = loadFirstFileLine(input).split(" ").filter { it.isNotBlank() }.map { it.toLong() }
    return (0L until blinkCount)
      .fold(stones) { acc, i ->
        val x = blink(acc)
        println("round $i >> ${x.count()}")
        System.gc()
        x
      }
      .count()
  }

  fun solution2(fileName: String): Long = 0

  fun splitStone(stone: Long): List<Long> {
//    println("Given [$stone] - 0 | ${stone.length/2} | ${stone.length}")
    val s = stone.toString()
    return listOf(
      s.substring(0, s.length / 2).toLong(),
      s.substring(s.length / 2, s.length).toLong()
    )
  }

  fun changeStone(stone: Long): List<Long> {
    return when {
      stone == 0L -> listOf(1)
      stone.toString().length % 2 == 0 -> splitStone(stone)
      else -> listOf(stone * 2024L)
    }
  }

  fun blink(stones: String): List<Long> {
    return blink(stones.split(" ").filter { it.isNotBlank() }.map{it.toLong()})
  }

  fun blink(stones: List<Long>): List<Long> {
    return stones
      .map { changeStone(it) }
      .flatten()
  }

  @Test
  fun example1() {
    val fileName = "y2024_d11_example.txt"
    assertEquals("1 2024 1 0 9 9 2021976", "0 1 10 99 999".blink(this))
    assertEquals("253000 1 7", "125 17".blink(this))
    assertEquals("253 0 2024 14168", "253000 1 7".blink(this))
    assertEquals("512072 1 20 24 28676032", "253 0 2024 14168".blink(this))
    assertEquals("512 72 2024 2 0 2 4 2867 6032", "512072 1 20 24 28676032".blink(this))
    assertEquals("1036288 7 2 20 24 4048 1 4048 8096 28 67 60 32",
      "512 72 2024 2 0 2 4 2867 6032".blink(this))
    assertEquals("2097446912 14168 4048 2 0 2 4 40 48 2024 40 48 80 96 2 8 6 7 6 0 3 2",
      "1036288 7 2 20 24 4048 1 4048 8096 28 67 60 32".blink(this))
    assertEquals(22, solution1(fileName, 6))
    assertEquals(55312, solution1(fileName, 25))

  }

  @Test
  fun mine1() {
    val fileName = "y2024_d11_mine.txt"
    assertEquals(1, solution1(fileName, 25))
  }

  @Test
  fun example2() {
    assertEquals(-1, solution2("y2024_d11_example.txt"))
  }

  @Test
  fun mine2() {
    val fileName = "y2024_d11_mine.txt"
    assertEquals(1, solution1(fileName, 75))
  }
}

fun String.blink(w: Y2024D11): String = w.blink(this).joinToString(" ")