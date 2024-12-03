package app.sixarmstudios.adventofcode

import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import kotlin.math.abs

class Y2024D01 {
  private fun loadFile(fileName: String): String {
    val inputStream = this.javaClass.classLoader!!.getResourceAsStream(fileName)
    return BufferedReader(InputStreamReader(inputStream)).readText()
  }

  private fun solve_1(fileName: String): Int {
    val list1 = mutableListOf<Int>()
    val list2 = mutableListOf<Int>()
    loadFile(fileName)
      .split("\n")
      .map { it.trim() }
      .filter { it.isNotBlank() }
      .forEach {
        val parts = it.split("   ")
        list1.add(parts[0].toInt())
        list2.add(parts[1].toInt())
      }
    list1.sort()
    list2.sort()
    val list3 = list1.mapIndexed { i, l1 ->
      abs(l1 - list2[i])
    }
    println("Given $list1 & $list2 --> $list3")
    return list3.sum()
  }


  private fun solve_2(fileName: String): Int {
    val list1 = mutableListOf<Int>()
    val list2 = mutableListOf<Int>()
    loadFile(fileName)
      .split("\n")
      .map { it.trim() }
      .filter { it.isNotBlank() }
      .forEach {
        val parts = it.split("   ")
        list1.add(parts[0].toInt())
        list2.add(parts[1].toInt())
      }
    val m1 = mutableMapOf<Int, Int>()
    list2.forEach { n ->
      m1.put(n, m1.getOrElse(n){0} + 1)
    }
    val list3 = list1.mapIndexed { i, l1 ->
      l1 * m1.getOrElse(l1){0}
    }
    println("Given $list1 & $list2 --> $list3")
    return list3.sum()
  }

  @Test
  fun example_1() {
    val fileName = "y2024_d01_example.txt"
    assertEquals(11, solve_1(fileName))
  }
  @Test
  fun mine_1() {
    val fileName = "y2024_d01_mine.txt"
    assertEquals(11, solve_1(fileName))
  }

  @Test
  fun example_2() {
    val fileName = "y2024_d01_example.txt"
    assertEquals(31, solve_2(fileName))
  }
  @Test
  fun mine_2() {
    val fileName = "y2024_d01_mine.txt"
    assertEquals(0, solve_2(fileName))
  }
}