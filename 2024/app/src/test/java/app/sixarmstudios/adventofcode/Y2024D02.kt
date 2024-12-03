package app.sixarmstudios.adventofcode

import junit.framework.TestCase.assertEquals
import org.junit.Test
import app.sixarmstudios.adventofcode.Shark
import kotlin.math.abs

class Y2024D02 : Shark() {
  private fun processReport_1(input: String): Boolean {
    var isIncreasingOuter: Boolean? = null
    input.split(" ")
      .map { it.toInt() }
      .foldIndexed(0) { i, acc, n ->
        val isIncreasing = isIncreasingOuter
        when {
          i == 0 -> return@foldIndexed n
          acc == n -> return false
          abs(acc - n) > 3 -> return false
          isIncreasing == null -> isIncreasingOuter = n > acc
          isIncreasing && n < acc -> return false
          !isIncreasing && n > acc -> return false
        }
        n
      }
    return true
  }

  /*
    private fun recursiveRun2(
    list: List<Int>,
    isIncreasing: Boolean,
    i: Int,
    prev: Int
  ): Boolean {
    val cur = list[i]
    val next = list.getOrNull(i + 1) ?: 0 // !!!!!
    val deltaPrev = validDelta(cur - prev, isIncreasing)
    val deltaNext = validDelta(next - prev, isIncreasing)
    val deltaSkip = validDelta(next - prev, isIncreasing)
    when {
      deltaPrev && deltaNext -> {
        return recursiveRun2(list, isIncreasing, i + 1, cur)
      }

      !deltaNext && !deltaPrev -> return false
      deltaPrev ->
    }
  }
   */

  private fun processReport_2(input: List<Int>, canSkip: Boolean): Boolean {
    var isIncreasingOuter: Boolean? = null
    var hasSkipLeft = canSkip
    input
      .foldIndexed(0) { i, acc, n ->
        val isIncreasing = isIncreasingOuter
        when {
          i == 0 -> return@foldIndexed n
          acc == n -> {
            if (hasSkipLeft) {
              hasSkipLeft = false
              return@foldIndexed acc
            } else {
              return false
            }
          }

          abs(acc - n) > 3 -> {
            if (hasSkipLeft) {
              hasSkipLeft = false
              return@foldIndexed acc
            } else {
              return false
            }
          }

          isIncreasing == null -> isIncreasingOuter = n > acc
          isIncreasing && n < acc -> {
            if (hasSkipLeft) {
              hasSkipLeft = false
              return@foldIndexed acc
            } else {
              return false
            }
          }

          !isIncreasing && n > acc -> {
            if (hasSkipLeft) {
              hasSkipLeft = false
              return@foldIndexed acc
            } else {
              return false
            }
          }
        }
        n
      }
    return return true
  }


  private fun printList(v:List<Int>, skipper:Int) =
    v.mapIndexed { i, x -> if (i == skipper) "[$x]" else "$x" }.joinToString(", ")


  private fun processReport_2a(input: List<Int>, skipIndex: Int?): Boolean {
    var isIncreasingOuter: Boolean? = null
    val handleBad: (Int) -> Boolean = { i ->
      if (skipIndex == null) {
        if (processReport_2a(input, i)) {
          println("skipping $i saved me\t\t\t\t ${printList(input, i)}")
          true
        } else if (i > 0) {
          val r = processReport_2a(input, i - 1)
          if (r) {
            println("skipping back to $i saved me\t\t ${printList(input, i-1)}")
            true
          } else {
            println("### error w/ $i\t\t\t\t ${printList(input, i - 1)}")
            false
          }
        } else {
          println("~~~ error w/ $i\t\t\t\t\t ${printList(input, i)}")
          false
        }
      } else {
        println("!!!!!!!!! $i\t\t\t\t\t ${printList(input, i)}")
        false
      }
    }
    input
      .foldIndexed(0) { i, acc, n ->
        if (skipIndex == i) {
          return@foldIndexed acc
        }
        val isIncreasing = isIncreasingOuter
        when {
          i == 0 -> return@foldIndexed n
          acc == n -> return handleBad(i)
          abs(acc - n) > 3 -> return handleBad(i)
          isIncreasing == null -> isIncreasingOuter = n > acc
          isIncreasing && n < acc -> return handleBad(i)
          !isIncreasing && n > acc -> return handleBad(i)
        }
        n
      }
    return true
  }

  private fun processReport_2a(input: String): Boolean {
    val values = input.split(" ")
      .map { it.toInt() }
    val result1 = processReport_2(values, true)
    if (result1) {
      return true
    }
    val newValues = values.toMutableList().also { it.removeFirst() }
    val result2 = processReport_2(newValues, false)
    if (!result2) {
      var badCounter = 0
      var inc = 0
      var dec = 0
      val msg = values.foldIndexed("") { i, acc, v ->
        val d = values.getOrNull(i - 1)?.let { v - it }?.let { d ->
          if (d == 0 || abs(d) > 3) {
            ++badCounter
            "~$d~"
          } else if (d > 0) {
            ++inc
            "($d)"
          } else {
            ++dec
            "[$d]"
          }
        } ?: ""
        "$acc $d $v"
      }
      if (badCounter <= 1)
        println("twice failed {$badCounter |  $dec / $inc }\t\t$msg")
    }
    return result2
  }

  private fun processReport_2(input: String): Boolean {
    val values = input.split(" ")
      .map { it.toInt() }
    val result1 = processReport_2(values, true)
    if (result1) {
      return true
    }
    val newValues = values.toMutableList().also { it.removeFirst() }
    val result2 = processReport_2(newValues, false)
    if (!result2) {
      var badCounter = 0
      var inc = 0
      var dec = 0
      val msg = values.foldIndexed("") { i, acc, v ->
        val d = values.getOrNull(i - 1)?.let { v - it }?.let { d ->
          if (d == 0 || abs(d) > 3) {
            ++badCounter
            "~$d~"
          } else if (d > 0) {
            ++inc
            "($d)"
          } else {
            ++dec
            "[$d]"
          }
        } ?: ""
        "$acc $d $v"
      }
      if (badCounter <= 1)
        println("twice failed {$badCounter |  $dec / $inc }\t\t$msg")
    }
    return result2
  }

  private fun solve_1(fileName: String): Int {
    return processFileData(loadFile(fileName))
      .map { processReport_1(it) }
      .count { it }
  }


  private fun solve_2(fileName: String): Int {
    return processFileData(loadFile(fileName))
      .map {
        val values = it.split(" ")
          .map { it.toInt() }
        processReport_2a(values, null)
      }
      .count { it }
  }

  @Test
  fun example_1() {
    assertEquals(2, solve_1("y2024_d02_example.txt"))
  }

  @Test
  fun mine_1() {
    assertEquals(483, solve_1("y2024_d02_mine.txt"))
  }

  @Test
  fun example_2() {
    assertEquals(4, solve_2("y2024_d02_example.txt"))
  }

  @Test
  fun mine_2() {
    assertEquals(2, solve_2("y2024_d02_mine.txt"))
    // 515 is too low - I know I'm not checking removing previous val
    // 527 is too low
  }
}