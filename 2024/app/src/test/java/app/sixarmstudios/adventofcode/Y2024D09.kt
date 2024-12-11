package app.sixarmstudios.adventofcode

import junit.framework.TestCase.assertEquals
import org.junit.Test

class Y2024D09 : Shark() {

  private fun solution1(fileName:String): Int {
    return 1
  }
  private fun solution2(fileName: String): Int {
    return 0
  }
  @Test
  fun example1() {
    assertEquals(41, solution1("y2024_d09_example.txt"))
  }

  @Test
  fun mine1() {
    assertEquals(4778, solution1("y2024_d09_mine.txt"))
  }

  @Test
  fun example2() {
    assertEquals(6, solution2("y2024_d09_example.txt"))
  }

  @Test
  fun mine2() {
    assertEquals(0, solution2("y2024_d09_mine.txt"))
  }
}