package app.sixarmstudios.adventofcode2018

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Starting at 10:18am.
 * I might have shrieked a little at how frightening the input file is.
 * ... solved part 1 by 10:40am? Maybe it got too hard and they're resetting?
 *
 */
class Day8 {
    private val part1_input = "2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2"

    private fun part1_solution(input:String):Int {
        val parts = input.split(" ")
                .map { it.trim().toInt() }
        val results = mutableListOf<Node>()
        val graph = chew_part1(parts.toMutableList(), results)
        return results.sumBy { it.metadata.sum() }

    }

    private fun part2_solution(input:String):Int {
        val parts = input.split(" ")
                .map { it.trim().toInt() }
        val results = mutableListOf<Node>()
        val root = chew_part2(parts.toMutableList(), results)
        return root.getValue()

    }
    private fun chew_part2(input: MutableList<Int>, results:MutableList<Node>):Node{
        val childNodeCount = input.removeAt(0)
        val metaDataCount = input.removeAt(0)
        val metadata = mutableListOf<Int>()
        val children = mutableListOf<Node>()
        val node = Node(childNodeCount,  metadata,  children)
        for (i  in 0 until childNodeCount) {
            children.add(chew_part2(input, results))
        }
        for (i in 0 until metaDataCount) {
            metadata.add(input.removeAt(0))
        }
        results.add(node)
        return node
    }


    data class Node(val childCount: Int, val metadata: List<Int>, val children: List<Node>) {
        fun getValue():Int {
            if (childCount == 0)
                return metadata.sum()
            return metadata.mapIndexed { index, data ->
                if (data > children.size)
                    return@mapIndexed 0
                children[data-1].getValue()
            }.sum()
        }
    }

    private fun chew_part1(input: MutableList<Int>, results:MutableList<Node>){
        val childNodeCount = input.removeAt(0)
        val metaDataCount = input.removeAt(0)
        for (i  in 0 .. childNodeCount - 1) {
            chew_part1(input, results)
        }
        val metadata = mutableListOf<Int>()
        for (i in 0 .. metaDataCount - 1) {
            metadata.add(input.removeAt(0))
        }
        results.add(Node(childNodeCount, metadata, emptyList()))
    }

    @Test
    fun part1_solve_example() {
        assertThat(part1_solution(part1_input), `is`(138))
    }
    @Test
    fun part1_solve() {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream("day8_input.txt")
        val inputStr = BufferedReader(InputStreamReader(inputStream)).readText()
        inputStream.close()
        assertThat(part1_solution(inputStr), `is`(40848))
        // frighteningly easy! 10:40am
    }

    @Test
    fun part2_solve_example() {
        assertThat(part2_solution(part1_input), `is`(66))
    }
    @Test
    fun part2_solve() {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream("day8_input.txt")
        val inputStr = BufferedReader(InputStreamReader(inputStream)).readText()
        inputStream.close()
        assertThat(part2_solution(inputStr), `is`(34466))
        // well... I got 0 and guessed it at 10:52, mostly as a joke
        // with only a minor off by one error, I found the solution at 10:59pm
    }

}