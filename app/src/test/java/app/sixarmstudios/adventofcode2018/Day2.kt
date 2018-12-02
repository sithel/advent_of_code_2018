package app.sixarmstudios.adventofcode2018

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Day 2
 *
 * Starting at 9:42am
 *
 */
class Day2 {
    fun solve_part1(input:List<String>): Int {
        val counts = input.map { Pair(solve_part1_hasRepeatCount(it, 2), solve_part1_hasRepeatCount(it, 3)) }
        val twoers = counts.map { if (it.first) 1 else 0 }.sum()
        val threers = counts.map { if (it.second) 1 else 0 }.sum()
        return twoers * threers
    }

    fun solve_part1_hasRepeatCount(input:String, countNeeded:Int): Boolean {
        return input.split("")
                .filter { it.isNotBlank() }
                .groupBy { it }
                .filter { it.value.size == countNeeded }
                .isNotEmpty()
    }

    @Test
    fun sample1_part1() {
        assertThat(solve_part1_hasRepeatCount("abcdef", 2), `is`(false))
        assertThat(solve_part1_hasRepeatCount("abcdef", 3), `is`(false))
    }
    @Test
    fun sample2_part1() {
        assertThat(solve_part1_hasRepeatCount("bababc", 2), `is`(true))
        assertThat(solve_part1_hasRepeatCount("bababc", 3), `is`(true))
    }
    @Test
    fun sample3_part1() {
        assertThat(solve_part1_hasRepeatCount("abbcde", 2), `is`(true))
        assertThat(solve_part1_hasRepeatCount("abbcde", 3), `is`(false))
    }
    @Test
    fun sample4_part1() {
        assertThat(solve_part1_hasRepeatCount("abcccd", 2), `is`(false))
        assertThat(solve_part1_hasRepeatCount("abcccd", 3), `is`(true))
    }
    @Test
    fun sample5_part1() {
        assertThat(solve_part1_hasRepeatCount("aabcdd", 2), `is`(true))
        assertThat(solve_part1_hasRepeatCount("aabcdd", 3), `is`(false))
    }
    @Test
    fun sample6_part1() {
        assertThat(solve_part1_hasRepeatCount("abcdee", 2), `is`(true))
        assertThat(solve_part1_hasRepeatCount("abcdee", 3), `is`(false))
    }
    @Test
    fun sample7_part1() {
        assertThat(solve_part1_hasRepeatCount("ababab", 2), `is`(false))
        assertThat(solve_part1_hasRepeatCount("ababab", 3), `is`(true))
    }

    @Test
    fun example_part1() {
        assertThat(solve_part1(listOf("abcdef", "bababc","abbcde","abcccd","aabcdd","abcdee","ababab")), `is`(12))
    }

    @Test
    fun solution_part1() {
        // grabbed string at 10:01am
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream("day1_input.txt")
        val inputStr = BufferedReader(InputStreamReader(inputStream)).readText()
        inputStream.close()
        val input = inputStr.split("\n")
                .map { it.trim() }
                .filter { it.isNotBlank() }
        assertThat(solve_part1(input), `is`(116))
        // NOOOoooooo! 116 is WRONG! guessed at 10:03
    }
}