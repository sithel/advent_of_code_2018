package app.sixarmstudios.adventofcode2018

import junit.framework.TestCase
import org.junit.Test
import java.io.BufferedReader
import java.io.InputStreamReader

class Y2021D08 {

  @Test
  fun part1_sample() {
    TestCase.assertEquals(26, fuel("y2021_d08_sample.txt", true))
  }

  @Test
  fun part1_mine() {
    TestCase.assertEquals(284, fuel("y2021_d08_mine.txt", true))
  }

  @Test
  fun part2_sample() {
    TestCase.assertEquals(61229, fuel("y2021_d08_sample.txt", false))
  }

  fun fuel(fileName: String, justUniqueCount: Boolean): Int {
    val inputStream = this.javaClass.classLoader!!.getResourceAsStream(fileName)
    val rows = BufferedReader(InputStreamReader(inputStream)).readText()
      .split("\n")
      .filter { it.isNotBlank() }
      .map {
        val parts = it.split("|")
        Row(
          parts[0].split(" ").map { sortIt(it) }.filter { it.isNotBlank() },
          parts[1].split(" ").map { sortIt(it) }.filter { it.isNotBlank() }
        )
      }
    println(rows)
    if (justUniqueCount)
      return justTheCount(rows)
    return rows.map { it.calcOutput() }.sum()
//    return rows.take(2).last().calcOutput()
  }

  fun justTheCount(rows: List<Row>): Int {
    return rows.map { r ->
      r.output
        .mapNotNull { s ->
          if (Digit.values().filter { it.segmentCount == s.length }.size == 1) 1 else null
        }
        .sum()
    }.sum()
  }

  private fun sortIt(input: String): String {
    return input.split("").sorted().joinToString("").trim()
  }

  data class Row(val prelimDisplays: List<String>, val output: List<String>) {
    fun calcOutput(): Int {
      val segments = prelimDisplays + output
      println("==========================[calcOutput")
      val wireBase = mutableMapOf<String, MutableSet<Leg>>()
      listOf("a", "b", "c", "d", "e", "f", "g").forEach { wireBase[it] = mutableSetOf() }
      val possibilities = mutableMapOf<Leg, MutableSet<String>>()
      Leg.values().forEach {
        possibilities[it] = mutableSetOf()
      }
      segments.forEach { s ->
        println("For segment: $s")
        val optWires = s.split("").filter { it.isNotBlank() }.sorted().toSet()
        val digits = Digit.values().filter { it.segmentCount == s.length && it != Digit.D_8 }
        if (digits.size != 1)
          return@forEach
        solveForSingles(digits.first(), possibilities, wireBase, optWires)
      }
      println("\n\twire base-> $wireBase\n\tpossibilities-> $possibilities")
      solveForOneMissing(segments.filter { it.length == 6 }, possibilities, wireBase)
      println("\n\twire base-> $wireBase\n\tpossibilities-> $possibilities")
      println("Our segments for : $this")
      println(" ~~ \n${wireBase.entries.joinToString("\n")}")
      println(" >> \n${possibilities.entries.joinToString("\n")}")
      return output.map { decodeNumber(it, wireBase, possibilities) }.sum()
    }

    private fun decodeNumber(display: String, wireOpt: MutableMap<String, MutableSet<Leg>>, legOpt: MutableMap<Leg, MutableSet<String>>): Int {
      val legs = display.map { wireOpt[it.toString()]?.first() }.toSet()
      println("Given $display : $legs")
      val top = legOpt[Leg.TOP]?.let {if(display.contains(it.first().toString())) it.joinToString("!") else "."} ?: "X"
      val mid = legOpt[Leg.MID]?.let {if(display.contains(it.first().toString())) it.joinToString("!") else "."} ?: "X"
      val bot = legOpt[Leg.BOT]?.let {if(display.contains(it.first().toString())) it.joinToString("!") else "."} ?: "X"
      val botL = legOpt[Leg.BOT_LEFT]?.let {if(display.contains(it.first().toString())) it.joinToString("!") else "."} ?: "X"
      val botR = legOpt[Leg.BOT_RIGTH]?.let {if(display.contains(it.first().toString())) it.joinToString("!") else "."} ?: "X"
      val topR = legOpt[Leg.TOP_RIGHT]?.let {if(display.contains(it.first().toString())) it.joinToString("!") else "."} ?: "X"
      val topL = legOpt[Leg.TOP_LEFT]?.let {if(display.contains(it.first().toString())) it.joinToString("!") else "."} ?: "X"
      println(" $top$top$top$top\n" +
        "$topL    $topR\n" +
        "$topL    $topR\n" +
        " $mid$mid$mid$mid\n" +
        "$botL    $botR\n" +
        "$botL    $botR\n" +
        " $bot$bot$bot$bot\n")
      return Digit.values().first { legs == it.legs }.decoded
    }

    private fun solveForOneMissing(displays: List<String>, legOpts: MutableMap<Leg, MutableSet<String>>, wireOpts: MutableMap<String, MutableSet<Leg>>) {
      //solve for 6
      println("Solve for [0,6,9] : $displays")
      val six = displays
        .firstOrNull() { display ->
          val allTop = legOpts[Leg.TOP_RIGHT]?.all { display.contains(it, true) } ?: false
          val allBot = legOpts[Leg.BOT_RIGTH]?.all { display.contains(it, true) } ?: false
          println("Does $display have allTop[${legOpts[Leg.TOP_RIGHT]}] ($allTop) && allBot[${legOpts[Leg.BOT_RIGTH]}] ($allBot) = ${!allTop || !allBot}")
          !allTop || !allBot
        }
        ?.let { s ->
          val botRight = legOpts[Leg.BOT_RIGTH]?.filter { s.contains(it) }?.firstOrNull()
          val topRight = legOpts[Leg.BOT_RIGTH]?.filter { !s.contains(it) }?.firstOrNull()
          println("~ $s in ${legOpts[Leg.BOT_RIGTH]} : $botRight / $topRight")
          legOpts[Leg.BOT_RIGTH]?.clear()
          botRight?.let { legOpts[Leg.BOT_RIGTH]?.add(it) }
          legOpts[Leg.TOP_RIGHT]?.clear()
          topRight?.let { legOpts[Leg.TOP_RIGHT]?.add(it) }
          wireOpts[botRight]?.clear()
          wireOpts[botRight]?.add(Leg.BOT_RIGTH)
          wireOpts[topRight]?.clear()
          wireOpts[topRight]?.add(Leg.TOP_RIGHT)
          println("We say bot = $botRight / top = $topRight")
          s
        }
      println("\n\twire base-> $wireOpts\n\tpossibilities-> $legOpts")
      println("My sixes : $six")
      val nine = displays
        .filter { it != six }
        .firstOrNull { display ->
          val overlap = six?.toSet()?.subtract(display.toSet())?.firstOrNull()?.toString()
          val hasMid = overlap?.let { legOpts[Leg.MID]?.contains(it) } ?: false
          if (hasMid && overlap != null) {
            wireOpts[overlap]?.clear()
            wireOpts[overlap]?.add(Leg.MID)
            val other = legOpts[Leg.MID]?.filter { it != overlap }
            other?.forEach { wireOpts[it]?.remove(Leg.MID) }
            other?.forEach{legOpts[Leg.MID]?.remove(it)}
            Leg.values().filter { it != Leg.MID }.forEach { legOpts[it]?.remove(overlap) }
          }
          println("Checking $display dif from  : ${overlap} : ${overlap?.let { legOpts[Leg.MID]?.contains(it) }}")
          hasMid
        }
      println("My nines : $nine")
      val zero = displays.firstOrNull { it != six && it != nine }
      println("My zero : $zero")

      println("\n\twire base-> $wireOpts\n\tpossibilities-> $legOpts")
      six?.let { s ->
        val wires = s.toSet().toMutableSet()
        legOpts[Leg.TOP]?.firstOrNull()?.let { wires.remove(it[0]) }
        legOpts[Leg.TOP_LEFT]?.firstOrNull()?.let { wires.remove(it[0]) }
        legOpts[Leg.MID]?.firstOrNull()?.let { wires.remove(it[0]) }
        legOpts[Leg.BOT_RIGTH]?.firstOrNull()?.let { wires.remove(it[0]) }
        legOpts[Leg.BOT]?.firstOrNull()?.let { wires.remove(it[0]) }
        println("after all that: $wires remains")
        wires.first().toString().let { wire -> //crash because we might need to use a 9?
          wireOpts[wire]?.add(Leg.BOT_LEFT)
          legOpts[Leg.BOT_LEFT]?.add(wire)
        }
      }
      println("\n\twire base-> $wireOpts\n\tpossibilities-> $legOpts")
      println("I'm looking at ${wireOpts.filter { it.value.isEmpty() }}")
      val remainingWire = wireOpts.filter { it.value.isEmpty() }.map { it.key }.first()
      wireOpts[remainingWire]?.add(Leg.BOT)
      legOpts[Leg.BOT]?.add(remainingWire)
    }

    private fun solveForSingles(digit: Digit, possibilities: MutableMap<Leg, MutableSet<String>>, wireBase: MutableMap<String, MutableSet<Leg>>, optWires: Set<String>) {
      val openLegs = digit.legs.filter { possibilities[it]!!.isEmpty() }
      optWires.forEach { wire ->
        println("  Testing  wire [$wire] / digit: $digit  ~ ${digit.legs}")
        wireBase[wire]?.let { legs ->
          println("For [$wire] -> open legs $openLegs   out of ${digit.legs}")
          if (legs.isEmpty())
            legs.addAll(openLegs)
        }
        val legOptionsSoFar = possibilities.entries.filter { it.value.contains(wire) }.map { it.key }
        val lettersClaimedSoFar = possibilities.values.flatten().toSet()
        println("    Letters claimed: $lettersClaimedSoFar  / legs so far: $legOptionsSoFar")
        val openWires = optWires.subtract(lettersClaimedSoFar)
        digit.legs.forEach legLoop@{ leg ->
          if (legOptionsSoFar.isNotEmpty() && !legOptionsSoFar.contains(leg)) {
            println("    we know $wire is not $leg")
            return@legLoop
          }
          possibilities[leg]?.let { letters ->
            if (letters.isEmpty())
              letters.addAll(openWires)
          }
        }
        if (digit == Digit.D_1) {
          wire.let {possibilities[Leg.TOP]?.remove(it)}
          wireBase[wire]?.remove(Leg.TOP)
        }
      }
      possibilities[Leg.TOP]?.firstOrNull()?.let { topWire ->
        possibilities[Leg.TOP_RIGHT]?.remove(topWire)
        possibilities[Leg.BOT_RIGTH]?.remove(topWire)
        wireBase[topWire]?.clear()
        wireBase[topWire]?.add(Leg.TOP)
      }
    }
/*
    fun calcOutput_v1(): Int {
      val possibilities = mutableMapOf<String, MutableSet<Leg>>()
      "abcdefg".split("").filter { it.isNotBlank() }.forEach { possibilities[it] = mutableSetOf() }
      segments.forEach { s ->
        println("For segment: $s")
        val optLegs = mutableListOf<Leg>()
        Digit.values()
          .filter { it.segmentCount == s.length }
          .forEach {
            println("  ~ $it : ${it.legs}")
            optLegs.addAll(it.legs)
          }
        s.split("").filter { it.isNotBlank() }.forEach { o ->
          val opts = possibilities[o]!!
          if (opts.isEmpty())
            opts.addAll(optLegs)
          else {
            val newSet = opts.intersect(optLegs)
            println("       [$o] conflict: $opts vs $optLegs    ---> ${newSet}")
            possibilities[o]!!.clear()
            possibilities[o]!!.addAll(newSet)
          }
          println("  > $o -> $opts")
        }
      }
      println("Our segments for : $this")
      println(" ~~ \n${possibilities.entries.joinToString("\n")}")
      return 5
    }
 */
  }

  enum class Digit(val decoded: Int, val segmentCount: Int, val legs: Set<Leg>) {
    D_0(0, 6, setOf(Leg.TOP, Leg.TOP_LEFT, Leg.TOP_RIGHT, Leg.BOT_LEFT, Leg.BOT_RIGTH, Leg.BOT)),
    D_1(1, 2, setOf(Leg.TOP_RIGHT, Leg.BOT_RIGTH)),
    D_2(2, 5, setOf(Leg.TOP, Leg.TOP_RIGHT, Leg.MID, Leg.BOT_LEFT, Leg.BOT)),
    D_3(3, 5, setOf(Leg.TOP, Leg.TOP_RIGHT, Leg.MID, Leg.BOT_RIGTH, Leg.BOT)),
    D_4(4, 4, setOf(Leg.TOP_LEFT, Leg.TOP_RIGHT, Leg.MID, Leg.BOT_RIGTH)),
    D_5(5, 5, setOf(Leg.TOP, Leg.TOP_LEFT, Leg.MID, Leg.BOT_RIGTH, Leg.BOT)),
    D_6(6, 6, setOf(Leg.TOP, Leg.TOP_LEFT, Leg.MID, Leg.BOT_LEFT, Leg.BOT_RIGTH, Leg.BOT)),
    D_7(7, 3, setOf(Leg.TOP, Leg.TOP_RIGHT, Leg.BOT_RIGTH)),
    D_8(8, 7, setOf(Leg.TOP, Leg.TOP_LEFT, Leg.TOP_RIGHT, Leg.MID, Leg.BOT_LEFT, Leg.BOT_RIGTH, Leg.BOT)),
    D_9(9, 6, setOf(Leg.TOP, Leg.TOP_LEFT, Leg.TOP_RIGHT, Leg.MID, Leg.BOT_RIGTH, Leg.BOT)),
  }

  enum class Leg {
    TOP, TOP_LEFT, TOP_RIGHT, MID, BOT_LEFT, BOT_RIGTH, BOT
  }
}