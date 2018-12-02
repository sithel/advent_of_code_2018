package app.sixarmstudios.adventofcode2018

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Day 2
 *
 * Looking at those strings... I mean box IDs...
 * 
 * Starting at 9:42am
 * Starting part 2 at 10:08am
 * Finished part 2 at 10:55am because of problems with the IDE/debugging....
 */
class Day2 {
    private fun solve_part1(input: List<String>): Int {
        val counts = input.map { Pair(solve_part1_hasRepeatCount(it, 2), solve_part1_hasRepeatCount(it, 3)) }
        val twoers = counts.map { if (it.first) 1 else 0 }.sum()
        val threers = counts.map { if (it.second) 1 else 0 }.sum()
        return twoers * threers
    }

    private fun solve_part2(input: List<String>): String {
        input.forEachIndexed { i1, id1 ->
            input.subList(i1 + 1, input.size)
                    .forEach { id2 ->
                        val common = solve_part2_buildCommonList(id1, id2)
                        if (common.length == id1.length - 1)
                            return common
                    }
        }
        return "FAILURE"
    }

    private fun solve_part2_buildCommonList(str1: String, str2: String): String {
        val parts1 = str1.split("")
                .filter { it.isNotBlank() }
        return str2.split("")
                .filter { it.isNotBlank() }
                .foldIndexed("") { index, prevStr, letter ->
                    // for the FUCKING record, Android Studio was showing an off by 1 error in the debugger for the index
                    // made debugging a BITCH
                    if (letter == parts1.getOrNull(index))
                        "$prevStr$letter"
                    else
                        prevStr
                }
    }

    // region part1
    fun solve_part1_hasRepeatCount(input: String, countNeeded: Int): Boolean {
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
        assertThat(solve_part1(listOf("abcdef", "bababc", "abbcde", "abcccd", "aabcdd", "abcdee", "ababab")), `is`(12))
    }

    @Test
    fun solution_part1() {
        // grabbed string at 10:01am
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream("day2_input.txt")
        val inputStr = BufferedReader(InputStreamReader(inputStream)).readText()
        inputStream.close()
        val input = inputStr.split("\n")
                .map { it.trim() }
                .filter { it.isNotBlank() }
        assertThat(solve_part1(input), `is`(8610))
        // NOOOoooooo! 116 is WRONG! guessed at 10:03
        // FML, I used the wrong input file... the danger of copy and paste (10:08am)
    }

    //endregion

    @Test
    fun part2_helper2off() {
        assertThat(solve_part2_buildCommonList("abcde", "axcye"), `is`("ace"))
    }

    @Test
    fun part2_helper1off() {
        assertThat(solve_part2_buildCommonList("fghij", "fguij"), `is`("fgij"))
    }

    @Test
    fun part2_helper0off() {
        assertThat(solve_part2_buildCommonList("klmno", "klmno"), `is`("klmno"))
    }

    @Test
    fun example_part2() {
        assertThat(solve_part2(listOf("abcde", "fghij", "klmno", "pqrst", "fguij", "axcye", "wvxyz")), `is`("fgij"))
    }

    @Test
    fun solution_part2() {
        // grabbed string at 10:54am
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream("day2_input.txt")
        val inputStr = BufferedReader(InputStreamReader(inputStream)).readText()
        inputStream.close()
        val input = inputStr.split("\n")
                .map { it.trim() }
                .filter { it.isNotBlank() }
        assertThat(solve_part2(input), `is`("iosnxmfkpabcjpdywvrtahluy"))
        // correct on the first try, 10:54am
    }
}