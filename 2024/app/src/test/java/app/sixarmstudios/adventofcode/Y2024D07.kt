package app.sixarmstudios.adventofcode

import junit.framework.TestCase.assertEquals
import org.junit.Test
import kotlin.collections.plus

class Y2024D07 : Shark() {

  data class Equation(val result: Long, val values: List<Long>)

  fun convertToEquation(s: String): Equation {
    val parts = s.split(" ").filter { it.isNotBlank() }
    return Equation(
      parts[0].substring(0, parts[0].length - 1).toLong(),
      parts.subList(1, parts.size).map { it.toLong() }
    )
  }

  fun apply(operator: String, v1: Long, v2: Long): Long = when (operator) {
    "*" -> v1 * v2
    "+" -> v1 + v2
    "||" -> "$v1$v2".toLong() // breaks part 1
    else -> throw IllegalStateException("wtf??? invalid op [$operator]")
  }

  fun isValid(e: Equation, operators: List<String>): Boolean {
//    println("checking $operators against $e")
    val result = operators.foldIndexed(e.values[0]) { i, acc, o ->
      val next = e.values[i + 1]
      val result = apply(o, acc, next)
      if (result > e.result) {
//        println(" --> bailing @ $i   [$acc $o $next] because $result > ${e.result}")
        return false
      }
      result
    }
    return if (operators.size == e.values.size - 1) {
//      println(" ---> ${result == e.result} = $result == ${e.result}")
      result == e.result
    } else {
//      println(" ----> trying another round")
      isValid(e, operators + listOf("+"))
        || isValid(e, operators + listOf("*"))
        || isValid(e, operators + listOf("||"))
    }
  }

  fun canBeValid(e: Equation): Boolean {
    return isValid(e, listOf("+"))
      || isValid(e, listOf("*"))
      || isValid(e, listOf("||"))
  }

  fun solution1(fileName: String): Long {
    return processFileData(loadFile(fileName))
      .map { convertToEquation(it) }
      .filter { canBeValid(it) }
      .map { it.result }
      .sum()
  }

  fun solution2(fileName: String): Int = 0

  @Test
  fun example1() {
    assertEquals(Equation(10, listOf(6, 10, 555)), convertToEquation("10: 6 10 555"))
    assertEquals(true, canBeValid(Equation(3267, listOf(81, 40, 27))))
    assertEquals(false, canBeValid(Equation(21037, listOf(9, 7, 18, 13))))
    val fileName = "y2024_d07_example.txt"
    assertEquals(3749, solution1(fileName))
  }

  @Test
  fun mine1() {
    val fileName = "y2024_d07_mine.txt"
    assertEquals(-1, solution1(fileName))
  }

  @Test
  fun example2() {
    assertEquals(11387, solution1("y2024_d07_example.txt"))
  }

  @Test
  fun mine2() {
    assertEquals(-1, solution1("y2024_d07_mine.txt"))
  }
}