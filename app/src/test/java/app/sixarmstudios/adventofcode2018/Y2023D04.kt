package app.sixarmstudios.adventofcode2018

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.coroutines.coroutineContext

class Y2023D04 {
  @OptIn(ExperimentalCoroutinesApi::class)
  private fun CoroutineScope.produceFileLines(fileName: String) = produce<String> {
    val inputStream = this.javaClass.classLoader!!.getResourceAsStream(fileName)
    BufferedReader(InputStreamReader(inputStream)).readText()
      .split("\n")
      .forEachIndexed { i, l -> println("[${Thread.currentThread().name}] Sending $i");send(l) }
    close()
  }

  data class Card(val game: Int, val winners: Set<Int>, val haves: Set<Int>) {
    fun winnerCount(): Int {
      return winners.intersect(haves).count()
    }
  }

  private fun convertLine(line: String): Card {
    val p1 = line.split(":")
    val game = Integer.parseInt(p1[0].trim().substring("Card ".length))
    val p2 = p1[0].split(" | ")
    val winners = p2[0].trim().split(" ").map { Integer.parseInt(it) }.toSet()
    val haves = p2[1].trim().split(" ").map { Integer.parseInt(it) }.toSet()
    return Card(game, winners, haves)
  }

  private suspend fun processLine(i: Int, lines: ReceiveChannel<String>, channel: Channel<Int>) {
    println("[${Thread.currentThread().name}] Receiving $i start")
    for (line in lines) {
      println("[${Thread.currentThread().name}]  > $i received $line")
      channel.send(convertLine(line).winnerCount())
    }
    println("[${Thread.currentThread().name}] Receiving $i escaped the loop")
  }

  private suspend fun calcResults(fileName: String): Int {
    println("[${Thread.currentThread().name}] pre-line allocation")
    with(CoroutineScope(coroutineContext)) {
      val lines = produceFileLines(fileName)
      val channel = Channel<Int>(5)
      println("[${Thread.currentThread().name}] pre-repeat")
      repeat(5) { processLine(it, lines, channel) }
      var total = 0
      for (result in channel) {
        total += result
        println("[${Thread.currentThread().name}]    >> updated total $total")
      }
      return total
    }
  }

  @Test
  fun shark() {
    assertEquals(true, false)
  }

  @Test
  fun part1_sample() = runTest {
    println("[${Thread.currentThread().name}] Test start")
    val results = calcResults("y2023_d04.sample.txt")
    println("[${Thread.currentThread().name}] Results in hand")
    assertEquals(13, results)
  }
}