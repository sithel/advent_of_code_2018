package app.sixarmstudios.adventofcode2018

import android.util.Range
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.io.BufferedReader
import java.io.InputStreamReader

class Y2023D04 {
  @OptIn(ExperimentalCoroutinesApi::class)
  private fun CoroutineScope.produceFileLines(fileName: String) = produce<String> {
    val inputStream = this.javaClass.classLoader!!.getResourceAsStream(fileName)
    println("[${Thread.currentThread().name}] about to start sending things!")
    BufferedReader(InputStreamReader(inputStream)).readText()
      .split("\n")
      .filter { it.isNotBlank() }
      .forEachIndexed { i, l ->
        println("[${Thread.currentThread().name}] Sending $i : $l");send(l);println(" XXX")
      }
    println("[${Thread.currentThread().name}] done, and about to close sending!")
    close()
  }

  data class Card(val game: Int, val winners: Set<Int>, val haves: Set<Int>) {
    fun winnerCount(): Int {
      return winners.intersect(haves).fold(0){acc, i ->
        if (acc == 0)
          1
        else
          acc * 2
      }
    }
  }

  private fun convertLine(line: String): Card {
    (0..10).forEach {  }
    val p1 = line.split(":")
    val game = Integer.parseInt(p1[0].trim().substring("Card ".length))
    val p2 = p1[1].split(" | ")
    val winners =
      p2[0].trim().split(" ").filter { it.isNotBlank() }.map { Integer.parseInt(it) }.toSet()
    val haves =
      p2[1].trim().split(" ").filter { it.isNotBlank() }.map { Integer.parseInt(it) }.toSet()
    return Card(game, winners, haves)
  }

  private suspend fun processLine(i: Int, lines: ReceiveChannel<String>, channel: MutableSharedFlow<Int>) {
    println("[${Thread.currentThread().name}] Receiving $i start")
    lines.receiveAsFlow().collect { value ->
      val r = convertLine(value).winnerCount()
      println("[${Thread.currentThread().name}]  > $i calced $r for $value")
      channel.emit(r)
      println("- end of a [$i] receive flow loop")
    }
    println("[${Thread.currentThread().name}]  > $i post flow")
  }


  private suspend fun calcResults(fileName: String): Int {
    println("[${Thread.currentThread().name}] pre-line allocation")
    val numFlow = MutableSharedFlow<Int>()
    val scope = CoroutineScope(Dispatchers.Default)
    val job = scope.launch {
      val lines = produceFileLines(fileName)
      println("[${Thread.currentThread().name}] pre-repeat")
      val jobs = mutableListOf<Job>()
      repeat(3) {
        jobs.add(launch {
          processLine(it, lines, numFlow)
        })
      }
      jobs.joinAll()
    }
    println("[${Thread.currentThread().name}] post-repeat, pre-join")
    var total = 0
    job.join()
    println("[${Thread.currentThread().name}] post-join, pre-result")
    numFlow.onEach { result ->
      total += result
      println("[${Thread.currentThread().name}]    >> updated w/ $result, total = $total")
    }
    println("[${Thread.currentThread().name}] we have our final total! $total")
    return total
  }

  @Test
  fun part1_sample() = runTest {
    println("[${Thread.currentThread().name}] Test start")
    val results = calcResults("y2023_d04.sample.txt")
    println("[${Thread.currentThread().name}] Results in hand")
    assertEquals(13, results)
  }
  @Test
  fun part1_mine() = runTest {
    println("[${Thread.currentThread().name}] Test start")
    val results = calcResults("y2023_d04.mine.txt")
    println("[${Thread.currentThread().name}] Results in hand")
    assertEquals(-1, results)
  }
}