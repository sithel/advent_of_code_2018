package app.sixarmstudios.adventofcode2018

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

/**
 *
 * The device then produces a list of coordinates (your puzzle input). Are they places it thinks
 * are safe or dangerous? It recommends you check manual page 729. The Elves did not give you
 * a manual.
 *
 * Starting at 8:13pm. Watching Critical Role. Stoned. Lets see how this goes...
 *      8:29, oh no, I have a horrible n^2 solution... and it seemed reasonable for a moment
 */
class Day6 {
    data class Locs(val distance : Int, val index : Int)
    fun part1_solution(input:String):Int {
        val points = part1_makePoints(input)
        val maxFirst = points.maxBy { it.first }!!.first
        val maxSecond = points.maxBy { it.second }!!.second
        val map = Array(maxFirst, { IntArray(maxSecond) })

        expand(points, map, 0, mutableListOf<Locs>())
        return 2
    }

    private fun expand(points: List<Pair<Int, Int>>, map: Array<IntArray>, i: Int, queue: MutableList<Locs>) {

    }

    private fun part1_makePoints(input: String): List<Pair<Int,Int>> = input.split("\n")
            .filter { it.isNotBlank() }
            .map {
                val p = it.trim().split(", ")
                Pair(p.first().toInt(), p.last().toInt())
            }

    @Test
    fun example1_findMaxs() {
        val input = "1, 1\n" +
                "1, 6\n" +
                "8, 333\n" +
                "3, 4\n" +
                "54, 5\n" +
                "8, 9"
        val maxes1 = part1_makePoints(input).maxBy { it.first }!!.first
        assertThat(maxes1, `is`(54))
        val maxes2 = part1_makePoints(input).maxBy { it.second }!!.second
        assertThat(maxes2, `is`(333))
    }
    @Test
    fun example1_test() {
        val input = "1, 1\n" +
                "1, 6\n" +
                "8, 3\n" +
                "3, 4\n" +
                "5, 5\n" +
                "8, 9"
        assertThat(part1_solution(input), `is`(17))
    }
}