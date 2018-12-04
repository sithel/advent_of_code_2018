package app.sixarmstudios.adventofcode2018

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Day 3
 *
 * Claims:
 * - left offset
 * - top offset
 * - width
 * - height
 *
 * #123 @ 3,2: 5x4
 *
 * Starting at 7:37am
 * Part 2 at 8:07am
 * Finished at 8:29am
 */
class Day3 {
    private val claimRegex = Regex("#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)")

    data class Claim(val id: Int, val leftOffset: Int, val topOffset: Int, val width: Int, val height: Int)

    private fun solve_part1(claims: List<String>): Int {
        return claims.map(this::parseClaim)
                .fold(Array(1000, { IntArray(1000) })) { fabric, claim ->
                    for (l in claim.leftOffset..claim.leftOffset + claim.width - 1) {
                        for (t in claim.topOffset..claim.topOffset + claim.height - 1) {
                            ++fabric[l][t]
                        }
                    }
                    fabric
                }
                .filter { it.isNotEmpty() }
                .map { row ->
                    row.filter { it > 1 }
                            .map { 1 }
                            .sum()
                }
                .sum()
    }


    private fun solve_part2(claims: List<String>): Int {
        val cList = claims.map(this::parseClaim)
        val map = cList
                .fold(Array(1000, { IntArray(1000) })) { fabric, claim ->
                    for (l in claim.leftOffset..claim.leftOffset + claim.width - 1) {
                        for (t in claim.topOffset..claim.topOffset + claim.height - 1) {
                            ++fabric[l][t]
                        }
                    }
                    fabric
                }
        return cList
                .filter { claim ->
                    for (l in claim.leftOffset..claim.leftOffset + claim.width - 1) {
                        for (t in claim.topOffset..claim.topOffset + claim.height - 1) {
                            if (map[l][t] != 1)
                                return@filter false
                        }
                    }
                    true
                }
                .first().id
    }

    private fun parseClaim(claim: String): Claim {
        val groups = claimRegex.matchEntire(claim)?.groups!!
        return Claim(groups[1]!!.value.toInt(),
                groups[2]!!.value.toInt(),
                groups[3]!!.value.toInt(),
                groups[4]!!.value.toInt(),
                groups[5]!!.value.toInt())
    }

    @Test
    fun claimRegex1_part1() {
        assertThat(parseClaim("#1 @ 1,3: 4x4"), `is`(Claim(1, 1, 3, 4, 4)))
    }

    @Test
    fun claimRegex2_part1() {
        assertThat(parseClaim("#123 @ 332,2121: 500x4114"), `is`(Claim(123, 332, 2121, 500, 4114)))
    }

    @Test
    fun example1_part1() {
        val claims = listOf("#1 @ 1,3: 4x4", "#2 @ 3,1: 4x4", "#3 @ 5,5: 2x2")
        assertThat(solve_part1(claims), `is`(4))
    }

    @Test
    fun example2_part1() {
        val claims = listOf("#123 @ 3,2: 5x4")
        assertThat(solve_part1(claims), `is`(0))
    }

    @Test
    fun solution_part1() {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream("day3_input.txt")
        val inputStr = BufferedReader(InputStreamReader(inputStream)).readText()
        inputStream.close()
        val input = inputStr.split("\n")
                .map { it.trim() }
                .filter { it.isNotBlank() }
        assertThat(solve_part1(input), `is`(119572))
        // right answer at 8:07! first try
    }

    @Test
    fun example1_part2() {
        val claims = listOf("#1 @ 1,3: 4x4", "#2 @ 3,1: 4x4", "#3 @ 5,5: 2x2")
        assertThat(solve_part2(claims), `is`(3))
    }

    @Test
    fun example2_part2() {
        val claims = listOf("#123 @ 3,2: 5x4")
        assertThat(solve_part2(claims), `is`(123))
    }

    @Test
    fun solution_part2() {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream("day3_input.txt")
        val inputStr = BufferedReader(InputStreamReader(inputStream)).readText()
        inputStream.close()
        val input = inputStr.split("\n")
                .map { it.trim() }
                .filter { it.isNotBlank() }
        assertThat(solve_part2(input), `is`(775))
        // right answer at 8:28... I went TOTALLY the wrong way in trying to solve this and had to restart :(
        // tried a dumb n^2 solution instead of USING THE DAMN MAP part 1 created

    }
}