package app.sixarmstudios.adventofcode

import junit.framework.TestCase.assertEquals
import org.junit.Test

class Y2024D06 : Shark() {
  var firstMark: Boolean = true
  var loopCount = 0
  fun solution2(fileName: String): Int {
    readMap(fileName)
      .let { traverseMap(it) }
      .let { countVisited(it) }
    return loopCount
  }


  fun solution1(fileName: String): Int {
    return readMap(fileName)
      .let { traverseMap(it) }
      .let { countVisited(it) }
  }

  private fun countVisited(map: List<List<String>>): Int {
    return map.map { r ->
      r.count { it == "x" || it == "+" || it == "-" || it == "|" }
    }.sum()
  }

  private fun moveGuard(
    info: Pair<Pair<Int, Int>, List<List<String>>>
  ): Pair<Pair<Int, Int>, List<List<String>>> {
    val guard = info.first.let { p -> info.second.getOrNull(p.first)?.getOrNull(p.second) }
    println(" [$guard] @ ${info.first}")
    return when (guard) {
      "^" -> runLine(info.first, 0, -1, ">", info.second)
      ">" -> runLine(info.first, 1, 0, "v", info.second)
      "v" -> runLine(info.first, 0, 1, "<", info.second)
      "<" -> runLine(info.first, -1, 0, "^", info.second)
      ".", null -> return info
      else -> throw IllegalStateException("where you pointing?? @ ${info.first} : $guard")
    }.let { newInfo ->
      println("      ~ now @ ${newInfo.first}")
      newInfo.second.forEach { r -> print("         "); r.forEach { print(it) };print("\n") }
      newInfo
    }
  }

  private fun runLine(
    start: Pair<Int, Int>,
    xDelta: Int,
    yDelta: Int,
    conclusionChar: String,
    map: List<List<String>>
  ): Pair<Pair<Int, Int>, List<List<String>>> {
    val nextLoc = Pair(start.first + yDelta, start.second + xDelta)
    var whatsNext = map.getOrNull(nextLoc.first)?.getOrNull(nextLoc.second)
    var whatsNow = map.getOrNull(start.first)?.getOrNull(start.second)
      ?: return Pair(start, map)
    val newMark: String = calcMark(xDelta, yDelta, whatsNext, whatsNow)
      ?: return Pair(start, map)
    if (!isGuard(whatsNow)) {
      checkForPotentialWall(start, xDelta, yDelta, map)
    }
    var newMap = markMap(start, newMark, map)
//    println("      > $whatsNext @ $nextLoc")
    return when (whatsNext) {
      "#" -> Pair(start, markMap(start, conclusionChar, newMap))
      null -> Pair(nextLoc, newMap)
      else -> return runLine(nextLoc, xDelta, yDelta, conclusionChar, newMap)
    }
  }

  fun checkForPotentialWall(
    start: Pair<Int, Int>,
    xDelta: Int,
    yDelta: Int,
    map: List<List<String>>
  ) {
    var lookAt = when {
      xDelta == 1 -> Pair(1, 0)
      xDelta == -1 -> Pair(-1, 0)
      yDelta == -1 -> Pair(0, 1)
      yDelta == 1 -> Pair(0, -1)
      else -> throw IllegalStateException("where we going??? $xDelta $yDelta")
    }
    var lookAlso = Pair(lookAt.first * -1, lookAt.second * -1)
    var potentialTurn = map
      .getOrNull(start.first + lookAt.first)
      ?.getOrNull(start.second + lookAt.second)
    var oppositeSide = map
      .getOrNull(start.first + lookAlso.first)
      ?.getOrNull(start.second + lookAlso.second)
//    println("     \uD83D\uDC40 $potentialTurn\t\t\t\t$xDelta, $yDelta")
    when (potentialTurn) {
      "|", "+", "-", "." -> Unit
      else -> return
    }
    var i = 1
    var seen: String? = "zzzz"
    while (seen != null) {
      var beyond = Pair(lookAt.first * i, lookAt.second * i)
      seen = map
        .getOrNull(start.first + beyond.first)
        ?.getOrNull(start.second + beyond.second)
      val past = Pair(lookAt.first * (i+1), lookAt.second * (i+1))
      val seenPast = map
        .getOrNull(start.first + past.first)
        ?.getOrNull(start.second + past.second)
//      println("              \uD83D\uDC40 $seen ($seenPast)\t\t\t\t$i")
      if (seen == "+" && seenPast == "#") {
//        println("⭐ potential WALL @ $start -> $potentialTurn | $oppositeSide " +
//          "(turn is $i steps away)")

//        val wallAt = Pair(start.first + yDelta, start.second + xDelta)
//        val endMap =  markMap(wallAt, "0", map)
//        endMap.forEach { r -> print("           "); r.forEach { print(it) };print("\n") }
        loopCount += 1
        return
      }
//      if (seen == ".") {
//        println("......falls wall @ $start -> $potentialTurn | $oppositeSide " +
//          "(escape is $i steps away)")
//        return
//      }
      i += 1
    }
  }

  private fun calcMark(
    xDelta: Int,
    yDelta: Int,
    whatsNext: String?,
    whatsNow: String?
  ): String? {
    whatsNext ?: return null
    whatsNow ?: return null
    return when {
      isGuard(whatsNow) && !firstMark -> "+"
      whatsNext == "#" -> "+"
      yDelta != 0 -> "|"
      else -> "-"
    }.let {
//      println(" ~ looking at $whatsNow/$whatsNext  [$firstMark] -> $it")
      firstMark = false
      it
    }

  }

  private fun markMap(loc: Pair<Int, Int>, s: String, map: List<List<String>>): List<List<String>> {
    return map.mapIndexed { r, row ->
      if (loc.first == r) {
        row.mapIndexed { c, existing -> if (loc.second == c) s else existing }
      } else {
        row
      }
    }
  }

  private fun traverseMap(info: Pair<Pair<Int, Int>, List<List<String>>>): List<List<String>> {
    var curPattern = info.second.toString()
    var newInfo = moveGuard(info)
    while (curPattern != newInfo.second.toString()) {
      curPattern = newInfo.second.toString()
      newInfo = moveGuard(newInfo)
    }
    println(" -- and we're out! w/ ${newInfo.first}\n")
    newInfo.second.forEach { r -> print("   | "); r.forEach { print(it) };print("\n") }
    return newInfo.second
  }

  private fun readMap(fileName: String): Pair<Pair<Int, Int>, List<List<String>>> {
    firstMark = true
    var start: Pair<Int, Int>? = null
    return processFileData(loadFile(fileName))
      .mapIndexed { r, s ->
        s.split("").filter { it.isNotBlank() }.mapIndexed { c, s2 ->
          if (isGuard(s2))
            start = Pair(r, c)
          s2.toString()
        }
      }
      .let { Pair(start!!, it) }
  }

  private fun isGuard(s: String): Boolean = s == "v" || s == ">" || s == "<" || s == "^"


  @Test
  fun example1() {
    assertEquals(Pair(6, 4), readMap("y2024_d06_example.txt").first)
    assertEquals(41, solution1("y2024_d06_example.txt"))
  }

  @Test
  fun mine1() {
    assertEquals(4778, solution1("y2024_d06_mine.txt"))
  }

  @Test
  fun example2() {
    assertEquals(6, solution2("y2024_d06_example.txt"))
  }

  @Test
  fun mine2() {
    assertEquals(0, solution2("y2024_d06_mine.txt"))
    // 711 is too low
  }
}