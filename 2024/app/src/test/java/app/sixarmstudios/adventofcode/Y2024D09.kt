package app.sixarmstudios.adventofcode

import junit.framework.TestCase.assertEquals
import org.junit.Test

class Y2024D09 : Shark() {

  private fun solution1(fileName: String): Long {
    val map = buildDiskMap(fileName)
    val pack = packDiskMap(map)
    return calcCheckSum(pack)
  }

  private fun solution2(fileName: String): Int {
    return 0
  }

  sealed class Block{
    data class Valid(val id: Int): Block()
    data object Invalid: Block()
  }

  private fun buildDiskMap(fileName: String): List<Block> {
    return processFileData(loadFile(fileName))[0]
      .split("")
      .filter { it.isNotBlank() }
      .mapIndexed { i, v ->
        val n = v.toInt()
        (0 until n).map {
          if (i % 2 == 0) {
            Block.Valid(i / 2)
          } else {
            Block.Invalid
          }
        }
      }
      .flatten()
  }

  private fun advnceToNextDotOrInfinity(start: Int, l: List<Block>): Int {
    return l.subList(start, l.size).indexOf(Block.Invalid)
      .let {
        if (it == -1)
          Int.MAX_VALUE
        else
          it + start
      }
  }

  private fun packDiskMap(input:List<Block>): List<Block> {
    return input.let{ list ->
        val l = list.toMutableList()
        var firstDot = advnceToNextDotOrInfinity(0, list)
        var lastNum = list.size - 1
        while (firstDot < lastNum) {
          if (l[lastNum] != Block.Invalid) {
            l[firstDot] = l[lastNum]
            l[lastNum] = Block.Invalid
            firstDot = advnceToNextDotOrInfinity(firstDot, l)
          }
          lastNum -= 1
        }
        l
      }
  }

  private fun calcCheckSum(input: List<Block>): Long {
    return input
      .foldIndexed(0L) { i, acc, s ->
        if (s !is Block.Valid)
          return@foldIndexed acc
        val sum = s.id * i
        acc + sum
      }
  }

  @Test
  fun example1() {
    val fileName = "y2024_d09_example.txt"
    println("start!")
    val map = buildDiskMap(fileName)
    println(" I see map : $map")
    val pack = packDiskMap(map)
    println(" I see packed : $pack")
//    assertEquals("00...111...2...333.44.5555.6666.777.888899", map)
//    assertEquals("0099811188827773336446555566..............", pack)
    assertEquals(1928, solution1(fileName))
  }

  @Test
  fun mine1() {
    val fileName = "y2024_d09_mine.txt"
    println("start!")
    val map = buildDiskMap(fileName)
    println(" I see map : $map")
    val pack = packDiskMap(map)
    println(" I see packed : $pack")
//    assertEquals(-1, pack)
    assertEquals(-1, solution1(fileName))
    // 89277257350 - too low
  }

  @Test
  fun example2() {
    assertEquals(-1, solution2("y2024_d09_example.txt"))
  }

  @Test
  fun mine2() {
    assertEquals(-1, solution2("y2024_d09_mine.txt"))
  }
}