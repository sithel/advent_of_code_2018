package app.sixarmstudios.adventofcode

import junit.framework.TestCase.assertEquals
import org.junit.Test

class Y2024D13 : Shark() {

  fun solution1(fileName: String): Int = 0
  fun solution2(fileName: String): Int = 0

  @Test
  fun example1() {
    val fileName = "y2024_d13_example.txt"
    assertEquals(-1, solution1(fileName))
  }

  @Test
  fun mine1() {
    val fileName = "y2024_d13_mine.txt"
    assertEquals(-1, solution1(fileName))
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