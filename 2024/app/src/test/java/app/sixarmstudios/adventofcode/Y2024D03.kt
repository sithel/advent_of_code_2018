package app.sixarmstudios.adventofcode

import junit.framework.TestCase.assertEquals
import org.junit.Test
import kotlin.math.min

class Y2024D03 : Shark() {


  /**
   * Starting at [start] (inclusive) - searches forward till it finds the entirely requested
   * string (exactly) -- stops once it encounters an invalid index OR reaches the end of the string
   * @return 1st invalid index OR next index after string completed or -1 if reaches
   *    the end w/o finding anything, whether it found the string
   */
  private fun searchFor(
    target: String, given: List<String>, start: Int, canScan: Boolean = false): Pair<Int, Boolean> {
//    println("given '$target' and '${given.joLongoString("-")}' -- $start")
    var stallStart = start
    while (canScan
      && given.getOrNull(stallStart) != target[0].toString()
      && given.size > stallStart
    ) {
//      println("wtf '${given.getOrNull(stallStart)}' vs '${target[0].toString()}' vs" +
//        " ${given.size} > $stallStart")
      ++stallStart
    }
    println("stall start : $stallStart")
    if (stallStart == given.size) {
      return Pair(-1, false)
    }
//    println(" start $start --> stallStart  $stallStart")
    for (i in 0 until min(target.length, stallStart + given.size)) {
//      println(">> '${given.getOrNull(i + stallStart)}' vs '${target.getOrNull(i)?.toString()}")
      if (given.getOrNull(i + stallStart) != target.getOrNull(i)?.toString()) {
        return Pair(stallStart + i, false)
      }
    }
//    println(" - success! ${stallStart + target.length}")
//    println(" ---> found! [$target] ~~ ${stallStart + target.length}")
    return Pair(stallStart + target.length, true)
  }

  private fun fetchSection(given: List<String>, start: Int, size: Int): String? {
    var result: String? = null
    for (i in start until size + start) {
      given.getOrNull(i)?.let { o ->
        result?.let { r -> result = r + o } ?: run { result = o }
      }
    }
    return result
  }

  /**
   * Starting at [start] (inclusive) - searches forward till it finds a 3, 2, or 1 digit number
   * -- will conclude after 3rd digit, even if followed by more
   * @return next index to evaluate, number (if found), whether the number is valid (in case of negative numbers)
   */
  private fun searchForNumber(given: List<String>, start: Int): Triple<Int, Long?, Boolean> {
    val a = fetchSection(given, start, 3)?.toLongOrNull()
//    println("a $a : $start :: |${fetchSection(given, start, 3)}| :::  $given :")
    if (a != null)
      return Triple(start + 3, a, true)
    val b = fetchSection(given, start, 2)?.toLongOrNull()
//    println("b $b")
    if (b != null)
      return Triple(start + 2, b, true)
    val c = fetchSection(given, start, 1)?.toLongOrNull()
//    println("c $c")
    if (c != null)
      return Triple(start + 1, c, true)
    return Triple(start + 3, null, false)
  }

  private fun pullFullMul(given: List<String>, start: Int): Pair<Long?, Int> {
    var (currentIndex1, isValid1) = searchFor("mul(", given, start, true)
    if (!isValid1) {
      println("\t\t\tfail on mul(  $currentIndex1 $isValid1")
      return Pair(null, currentIndex1)
    }
    var (currentIndex2, x, isValid2) = searchForNumber(given, currentIndex1)
    if (!isValid2 || x == null) {
      println("\t\t\tfail on x $currentIndex2 $isValid2")
      return Pair(null, currentIndex2)
    }
    var (currentIndex3, isValid3) = searchFor(",", given, currentIndex2)
    if (!isValid3) {
      println("\t\t\tfail on , $currentIndex3 $isValid3 $x")
      return Pair(null, currentIndex3)
    }
    var (currentIndex4, y, isValid4) = searchForNumber(given, currentIndex3)
    if (!isValid4 || y == null) {
      println("\t\t\tfail on y $currentIndex4 $isValid4")
      return Pair(null, currentIndex4)
    }
    var (currentIndex5, isValid5) = searchFor(")", given, currentIndex4)
//    println("I've got $isValid5 @ $currentIndex5 given $currentIndex4 ")
    if (!isValid5) {
      println("\t\t\tfail on ) $currentIndex5 $isValid5 [ $x - $y ]")
      return Pair(null, currentIndex5)
    }
    println("\t\t$x * $y = ${x * y}")
    return Pair(x * y, currentIndex5)
  }

  private fun findNextChar(given: List<String>, start: Int): Triple<Long?, Int, Boolean?> {
    val nextDo = searchFor("do()", given, start, true)
    val nDNum = nextDo.first.let { if (it == -1) Int.MAX_VALUE else it }
    val nextDont = searchFor("don't()", given, start, true)
    val nDtNum = nextDont.first.let { if (it == -1) Int.MAX_VALUE else it }
    val nextMul = pullFullMul(given, start)
    val nMNum = nextMul.second.let { if (it == -1) Int.MAX_VALUE else it }
    println("\tAudit :: $nextDo :: $nextDont :: $nextMul")
    if (nextDo.second && (!nextDont.second || nDNum < nDtNum) && ( nDNum < nMNum)) {
      println("DO")
      return Triple(null, nextDo.first, true)
    }
    if (nextDont.second && (!nextDo.second || nDtNum < nDNum) && ( nDtNum < nMNum)) {
      println("DON'T")
      return Triple(null, nextDont.first, false)
    }
    println("val> ${nextMul.first}")
    return Triple(nextMul.first, nextMul.second, null)
  }

  private fun solve_2(fileName: String): Long {
    return processFileData(loadFile(fileName)).joinToString("").let { s ->
      val input = s.replace("\n", "").split("")
        .filter { it.isNotBlank() }
      println("Given :: $input")

      var (result, curIndex, curEnable) = findNextChar(input, 0)
//      println("looking at initial $result & $curIndex")
      while (curIndex != -1) {
        var (newResult, newIndex, newEnable) = findNextChar(input, curIndex)
        println(">>.....  $newResult & $newIndex & $curEnable " +
          "( given $curIndex & $result & $newEnable)")
        if (newIndex > input.size) {
          break;
        }
        if (curIndex == newIndex) {
          ++newIndex
        }
        curIndex = newIndex
        newEnable?.let { curEnable = it }
        if (curEnable ?: true) {
          newResult?.let { n ->
            result?.let { r ->
              //            println(" >> @ $curIndex : $n ~~ $r")
              result = n + r
            } ?: run {
              println(" >> @ $curIndex : $n")
              result = n
            }
          }
        }
      }
//      println(">> we end with  $curIndex & $result")
      result ?: 0
    }
  }

  private fun solve_1(fileName: String): Long {
    return processFileData(loadFile(fileName)).joinToString("").let { s ->
      val input = s.replace("\n", "").split("")
        .filter { it.isNotBlank() }

      var (result, curIndex) = pullFullMul(input, 0)
//      println("looking at initial $result & $curIndex")
      while (curIndex != -1) {
        var (newResult, newIndex) = pullFullMul(input, curIndex)
        println(">>.....  $newResult & $newIndex ( given $curIndex & $result)")
        if (newIndex > input.size) {
          break;
        }
        if (curIndex == newIndex) {
          ++newIndex
        }
        curIndex = newIndex
        newResult?.let { n ->
          result?.let { r ->
//            println(" >> @ $curIndex : $n ~~ $r")
            result = n + r
          } ?: run {
            println(" >> @ $curIndex : $n")
            result = n
          }
        }
      }
//      println(">> we end with  $curIndex & $result")
      result ?: 0
    }
  }

  fun makeTS(s: String): List<String> = s.split("").filter { it.isNotBlank() }

  @Test
  fun example_1() {
//    assertEquals(Pair(3, true),searchFor("abc", "abc".split("").filter{it.isNotBlank()}, 0))
//    assertEquals(Pair(-1, false),searchFor("abc", "xxx".split("").filter{it.isNotBlank()}, 0))
//    assertEquals(Pair(3, true),searchFor("abc", "abcz".split("").filter{it.isNotBlank()}, 0))
//    assertEquals(Pair(4, true),searchFor("abc", "zabcz".split("").filter{it.isNotBlank()}, 0))
//    assertEquals(Pair(4, true),searchFor("abc", "zabcz".split("").filter{it.isNotBlank()}, 1))
//    assertEquals(Pair(-1, false),searchFor("abc", "zabcz".split("").filter{it.isNotBlank()}, 2))
//    assertEquals(Pair(4, true), searchFor("(", makeTS("mul(123,10"), 3))
//
//    assertEquals(Pair(1230, 11), pullFullMul(makeTS("mul(123,10)"), 0))
//    assertEquals(Pair(1230, 11), pullFullMul(makeTS("mul(123,10)22222"), 0))
//    assertEquals(Pair(5400, 11 + 3), pullFullMul(makeTS("123mul(54,100)22222"), 0))
//    assertEquals(Pair(1700, 11 + 3), pullFullMul(makeTS("123mul(17,100)22222"), 3))
//    assertEquals(Pair(null, -1), pullFullMul(makeTS("123mul(123,10)22222"), 4))
    assertEquals(161, solve_1("y2024_d03_example.txt"))
  }

  @Test
  fun mine_1() {
    assertEquals(155955228, solve_1("y2024_d03_mine.txt"))
    // 23968600 : your answer is too low
    // 155955228
  }


  @Test
  fun example_2() {
//    assertEquals(Pair(3, true),searchFor("abc", "abc".split("").filter{it.isNotBlank()}, 0))
//    assertEquals(Pair(-1, false),searchFor("abc", "xxx".split("").filter{it.isNotBlank()}, 0))
//    assertEquals(Pair(3, true),searchFor("abc", "abcz".split("").filter{it.isNotBlank()}, 0))
//    assertEquals(Pair(4, true),searchFor("abc", "zabcz".split("").filter{it.isNotBlank()}, 0))
//    assertEquals(Pair(4, true),searchFor("abc", "zabcz".split("").filter{it.isNotBlank()}, 1))
//    assertEquals(Pair(-1, false),searchFor("abc", "zabcz".split("").filter{it.isNotBlank()}, 2))
//    assertEquals(Pair(4, true), searchFor("(", makeTS("mul(123,10"), 3))
//
//    assertEquals(Pair(1230, 11), pullFullMul(makeTS("mul(123,10)"), 0))
//    assertEquals(Pair(1230, 11), pullFullMul(makeTS("mul(123,10)22222"), 0))
//    assertEquals(Pair(5400, 11 + 3), pullFullMul(makeTS("123mul(54,100)22222"), 0))
//    assertEquals(Pair(1700, 11 + 3), pullFullMul(makeTS("123mul(17,100)22222"), 3))
//    assertEquals(Pair(null, -1), pullFullMul(makeTS("123mul(123,10)22222"), 4))
    assertEquals(48, solve_2("y2024_d03_example2.txt"))
  }

  @Test
  fun mine_2() {
    assertEquals(1, solve_2("y2024_d03_mine.txt"))
    // 24397126 //your answer is too low
    // 100189366
  }

  /*

   3    9307   4703  *****   <-- start
   3   19065   6002  *******    <-- part 1
   3   22735   7061  ********     <-- part 2
   */
}