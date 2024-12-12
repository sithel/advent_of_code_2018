package app.sixarmstudios.adventofcode

import app.sixarmstudios.adventofcode.Y2024D07.Equation
import junit.framework.TestCase.assertEquals
import org.junit.Test

class Y2024D12 : Shark() {

  fun solution1(fileName: String): Int = 0
  fun solution2(fileName: String): Int = 0

  @Test
  fun example1() {
    val fileName = "y2024_d12_example.txt"
    assertEquals(-1, solution1(fileName))
  }

  @Test
  fun mine1() {
    val fileName = "y2024_d12_mine.txt"
    assertEquals(-1, solution1(fileName))
  }

  @Test
  fun example2() {
    assertEquals(11387, solution1("y2024_d12_example.txt"))
  }

  @Test
  fun mine2() {
    assertEquals(-1, solution1("y2024_d12_mine.txt"))
  }
}