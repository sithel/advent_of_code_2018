package app.sixarmstudios.adventofcode2018

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * https://adventofcode.com/2018/day/1
 *
 * Here are other example situations:
 *
 * +1, +1, +1 results in  3
 * +1, +1, -2 results in  0
 * -1, -2, -3 results in -6
 * Starting with a frequency of zero, what is the resulting frequency after all of the changes in frequency have been applied?
 *
 * Starting at 10:42am 12/1/2018
 */
class Day1 {
    private fun solve_part1(input: String): Int {
        return input.replace(",", "")
                .replace("+", "")
                .replace("\n"," ")
                .split(" ")
                .filter { it.isNotBlank() }
                .map { it.toInt() }
                .sum()
    }

    @Test
    fun sample1_part1() {
        assertThat(solve_part1("+1, +1, +1"), `is`(3))
    }

    @Test
    fun sample2() {
        assertThat(solve_part1("+1, +1, -2"), `is`(0))
    }

    @Test
    fun sample3_part1() {
        assertThat(solve_part1("-1, -2, -3"), `is`(-6))
    }

    @Test
    fun input_part1() {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream("day1_input.txt")
        val inputStr = BufferedReader( InputStreamReader(inputStream)).readText()
        inputStream.close()
        assertThat(solve_part1(inputStr), `is`(420))
        // 11:07am, first gold star
    }


}