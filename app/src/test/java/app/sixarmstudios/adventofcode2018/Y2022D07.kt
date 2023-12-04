package app.sixarmstudios.adventofcode2018

import junit.framework.TestCase
import org.junit.Test
import java.io.BufferedReader
import java.io.InputStreamReader

class Y2022D07 {
  //region Day 01
  @Test
  fun day01_p1_sample() {
    TestCase.assertEquals(24000, calcFood("y2022_d01_sample.txt"))
  }

  @Test
  fun day01_p1_mine() {
    TestCase.assertEquals(73211, calcFood("y2022_d01_mine.txt"))
  }

  @Test
  fun day01_p2_sample() {
    TestCase.assertEquals(45000, calcFood("y2022_d01_sample.txt", 3))
  }

  @Test
  fun day01_p2_mine() {
    TestCase.assertEquals(0, calcFood("y2022_d01_mine.txt", 3))
  }

  fun calcFood(fileName: String, take: Int = 1): Int {
    val inputStream = this.javaClass.classLoader!!.getResourceAsStream(fileName)
    val rows = BufferedReader(InputStreamReader(inputStream)).readText()
      .split("\n\n")
      .filter { it.isNotBlank() }
      .map { s ->
        s.split("\n")
          .filter { it.isNotBlank() }
          .map { Integer.parseInt(it) }
          .sum()
      }
    println(rows.joinToString(" - "))
    return rows.sortedDescending().take(take).sum() ?: 0
  }
  //endregion

  //region Day 02
  sealed class GameMove(val score: Int) {
    object Rock : GameMove(1)
    object Paper : GameMove(2)
    object Scissors : GameMove(3)
    companion object {
      fun parse(letter: Char): GameMove {
        return when (letter) {
          'X', 'A' -> Rock
          'Y', 'B' -> Paper
          'Z', 'C' -> Scissors
          else -> throw java.lang.IllegalStateException("Can't parse '$letter'")
        }
      }
      fun calcMove(theirMove:GameMove, outcome:EndGame): GameMove {
        return when(theirMove) {
          Paper -> when(outcome){
            EndGame.Draw -> Paper
            EndGame.Lose -> Rock
            EndGame.Win -> Scissors
          }
          Rock -> when(outcome){
            EndGame.Draw -> Rock
            EndGame.Lose -> Scissors
            EndGame.Win -> Paper
          }
          Scissors -> when(outcome){
            EndGame.Draw -> Scissors
            EndGame.Lose -> Paper
            EndGame.Win -> Rock
          }
        }
      }
    }
  }

  sealed class EndGame(val score: Int) {
    object Win : EndGame(6)
    object Lose : EndGame(0)
    object Draw : EndGame(3)
    companion object {
      fun parse(input:Char) : EndGame {
        return when(input) {
          'Y' -> Draw
          'Z' -> Win
          'X' -> Lose
          else -> throw java.lang.IllegalStateException("Can't parse '$input'")
        }
      }
      /**
       * @param moves - first should be opponent's, second should be yours
       */
      fun resolve(moves: Pair<GameMove, GameMove>): EndGame {
        return when (moves.first) {
          GameMove.Paper -> when (moves.second) {
            GameMove.Paper -> Draw
            GameMove.Rock -> Lose
            GameMove.Scissors -> Win
          }
          GameMove.Rock -> when (moves.second) {
            GameMove.Paper -> Win
            GameMove.Rock -> Draw
            GameMove.Scissors -> Lose
          }
          GameMove.Scissors -> when (moves.second) {
            GameMove.Paper -> Lose
            GameMove.Rock -> Win
            GameMove.Scissors -> Draw
          }
        }
      }
    }
  }

  @Test
  fun day02_p1_sample() {
    TestCase.assertEquals(15, playGame("y2022_d02_sample.txt"))
  }

  @Test
  fun day02_p1_mine() {
    TestCase.assertEquals(0, playGame("y2022_d02_mine.txt"))
  }

  @Test
  fun day02_p2_sample() {
    TestCase.assertEquals(12, playGameRound2("y2022_d02_sample.txt"))
  }
  @Test
  fun day02_p2_mine() {
    // 13885 too low
    TestCase.assertEquals(15702, playGameRound2("y2022_d02_mine.txt"))
  }


  fun playGame(fileName: String, take: Int = 1): Int {
    val inputStream = this.javaClass.classLoader!!.getResourceAsStream(fileName)
    val rows = BufferedReader(InputStreamReader(inputStream)).readText()
      .split("\n")
      .filter { it.isNotBlank() }
      .map { it.trim().let { s -> Pair(GameMove.parse(s[0]), GameMove.parse(s[2])) } }
      .map { EndGame.resolve(it).score + it.second.score }
    return rows.sum()
  }
  fun playGameRound2(fileName: String, take: Int = 1): Int {
    val inputStream = this.javaClass.classLoader!!.getResourceAsStream(fileName)
    val rows = BufferedReader(InputStreamReader(inputStream)).readText()
      .split("\n")
      .filter { it.isNotBlank() }
      .map { it.trim().let { s -> Pair(GameMove.parse(s[0]), EndGame.parse(s[2])) } }
      .map {
        val m = GameMove.calcMove(it.first, it.second)
//        println(" ${it.first.javaClass.simpleName} vs [[ ${m.javaClass.simpleName} ]] ==  ${it.second.javaClass.simpleName} ");
        m.score + it.second.score }
    return rows.sum()
  }
  //endregion


  @Test
  fun day03_p1_sample() {
    TestCase.assertEquals(157, lookInSack("y2022_d03_sample.txt"))
  }

  @Test
  fun day03_p1_mine() {
    TestCase.assertEquals(0, lookInSack("y2022_d03_mine.txt"))
  }

  fun lookInSack(fileName: String, take: Int = 1): Int {
    val inputStream = this.javaClass.classLoader!!.getResourceAsStream(fileName)
    val rows = BufferedReader(InputStreamReader(inputStream)).readText()
      .split("\n")
      .filter { it.isNotBlank() }
      .map { line ->
        val packA = line.subSequence(0, line.length/2)
        .split("")
        .filter { it.isNotBlank() }
          .toSet()
        val packB = line.subSequence(line.length/2, line.length)
          .split("")
          .filter { it.isNotBlank() }
          .toSet()
        val overlap = packA.intersect(packB)
//        println("Pack: [$packA] --- [$packB] :: $overlap :: [${overlap.map { it[0].toInt() }.joinToString(" ")}]")
        overlap.map { s ->
          if (s[0].isUpperCase()) {
            s[0].toInt() - 64 + 26//A : 65
          } else {
            s[0].toInt() - 96 //a : 97
          }
        }.map {
//          println("   > $it")
          it
        }.sum()
      }
    return rows.sum()
  }

}