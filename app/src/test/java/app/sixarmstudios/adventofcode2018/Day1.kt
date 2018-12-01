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
 * Starting part 2 at 11:09am 12/1/2018
 * Finished by 11:30am 12/1/2018
 */
class Day1 {
    private fun solve_part1(input: String): Int {
        return input.replace(",", "")
                .replace("+", "")
                .replace("\n", " ")
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
    fun sample2_part1() {
        assertThat(solve_part1("+1, +1, -2"), `is`(0))
    }

    @Test
    fun sample3_part1() {
        assertThat(solve_part1("-1, -2, -3"), `is`(-6))
    }

    @Test
    fun input_part1() {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream("day1_input.txt")
        val inputStr = BufferedReader(InputStreamReader(inputStream)).readText()
        inputStream.close()
        assertThat(solve_part1(inputStr), `is`(420))
        // 11:07am, first gold star
    }

    private fun solve_part2(input: String): Int {
        val list = input.replace(",", "")
                .replace("+", "")
                .replace("\n", " ")
                .split(" ")
                .filter { it.isNotBlank() }
                .map { it.toInt() }

        return findFreq(0, list, mutableSetOf(0))
    }

    private fun findFreq(startValue: Int, list: List<Int>, pastFreq: MutableSet<Int>): Int {
        val endValue = list.fold(startValue) { prevValue, acc ->
            val newFreq = prevValue + acc
            if (pastFreq.contains(newFreq))
                return newFreq  // bail HARD out of the function, we're done!
            pastFreq.add(newFreq)
            newFreq
        }
        return findFreq(endValue, list, pastFreq)
    }

    @Test
    fun sample1_part2() {
        assertThat(solve_part2("+1, -1"), `is`(0))
    }

    @Test
    fun sample2_part2() {
        assertThat(solve_part2("+3, +3, +4, -2, -4"), `is`(10))
    }

    @Test
    fun sample3_part2() {
        assertThat(solve_part2("-6, +3, +8, +5, -6"), `is`(5))
    }

    @Test
    fun sample4_part2() {
        assertThat(solve_part2("+7, +7, -2, -7, -4"), `is`(14))
    }

    @Test
    fun sample0_part2() {
        assertThat(solve_part2("+1, -2, +3, +1"), `is`(2))
    }

    @Test
    fun input_part2() {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream("day1_input.txt")
        val inputStr = BufferedReader(InputStreamReader(inputStream)).readText()
        inputStream.close()
        assertThat(solve_part2(inputStr), `is`(227))
        // done at 11:30am
    }


}