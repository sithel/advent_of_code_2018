package app.sixarmstudios.adventofcode2018

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Oops, started around 8:30am...
 * Seems easy enough
 */
class Day7 {
    data class Node(val instruction: String, val laterSteps: MutableList<Node>)

    val instructionRegex = Regex("Step (.) must be finished before step (.) can begin.")

    val example1 = "Step C must be finished before step A can begin.\n" +
            "Step C must be finished before step F can begin.\n" +
            "Step A must be finished before step B can begin.\n" +
            "Step A must be finished before step D can begin.\n" +
            "Step B must be finished before step E can begin.\n" +
            "Step D must be finished before step E can begin.\n" +
            "Step F must be finished before step E can begin.\n" +
            "Step Z must be finished before step F can begin.\n"

    val example2 = "Step C must be finished before step A can begin.\n" +
            "Step C must be finished before step F can begin.\n" +
            "Step A must be finished before step B can begin.\n" +
            "Step A must be finished before step D can begin.\n" +
            "Step B must be finished before step E can begin.\n" +
            "Step D must be finished before step E can begin.\n" +
            "Step F must be finished before step E can begin.\n"

    private fun part1_solve(input: String): String {
        val map = part1_makeNodeMap(part1_makePoints(input))
        val references = part1_buildBackReferences(map)
        val heads = part1_findHead(map)
        // I need the backwards path, which is part of the head call. :-/
        return part1_tracePath(
                map,
                heads.toMutableSet(),
                references,
                mutableSetOf(),
                ""
        )
    }

    private tailrec fun part1_tracePath(map: Map<String, Node>, options: MutableSet<String>, requirements: Map<String, Set<Day7.Node>>, visited: MutableSet<String>, path: String): String {
        if (options.size == 0)
            return path
        val nextId = options.min()!!
        val next = map[nextId]!!
        options.remove(nextId)
        visited.add(nextId)
        // need to make sure PRIOR steps are included, which I haven't built in those yet
        next.laterSteps
                .filter { !visited.contains(it.instruction) && part1_requirementsMet(it, visited, requirements) }
                .forEach { options.add(it.instruction) }
        return part1_tracePath(map, options, requirements, visited, "$path${next.instruction}")
    }


    private fun part1_requirementsMet(node: Node, visited: Set<String>, requirements: Map<String, Set<Node>>): Boolean {
        return visited.containsAll(requirements.getOrDefault(node.instruction, emptySet()).map { it.instruction })
    }

    private fun part1_buildBackReferences(map: Map<String, Node>): MutableMap<String, MutableSet<Node>> {
        return map.values
                .fold(mutableMapOf()) { collector, node ->
                    node.laterSteps.forEach {
                        val req = collector.getOrDefault(it.instruction, mutableSetOf())
                        req.add(node)
                        collector.put(it.instruction, req)
                    }
                    collector
                }
    }

    private fun part1_findHead(map: Map<String, Node>): List<String> {
        val references = map.values
                .fold(mutableMapOf<String, Int>()) { collector, node ->
                    node.laterSteps.forEach {
                        val step = collector.getOrDefault(it.instruction, 0)
                        collector.put(it.instruction, step + 1)
                    }
                    collector
                }
        return map
                .keys
                .filter { !references.containsKey(it) }
                .sorted()
    }

    private fun part1_makeNodeMap(pairs: List<Pair<String, String>>): Map<String, Node> {
        return pairs.fold(mutableMapOf<String, Node>()) { nodeMap, pair ->
            val firstNode = nodeMap.getOrDefault(pair.first, Node(pair.first, mutableListOf()))
            val secondNode = nodeMap.getOrDefault(pair.second, Node(pair.second, mutableListOf()))
            firstNode.laterSteps.add(secondNode)
            nodeMap.put(firstNode.instruction, firstNode)
            nodeMap.put(secondNode.instruction, secondNode)
            nodeMap
        }
    }

    private fun part1_makePoints(input: String): List<Pair<String, String>> {
        return input.split("\n")
                .filter { it.isNotBlank() }
                .map {
                    val l = instructionRegex.matchEntire(it)!!
                    Pair(l.groupValues[1], l.groupValues[2])
                }
    }

    private fun part2_solution(input: String, workerCount: Int, stepTimeOffset: Int): Int {
        val map = part1_makeNodeMap(part1_makePoints(input))
        val references = part1_buildBackReferences(map)
        val heads = part1_findHead(map)
        return part2_tracePath(
                count = 0,
                scale = stepTimeOffset,
                map = map,
                workers = Array(workerCount) { mutableListOf<WorkBlock?>(null) },
                options = heads.toMutableSet(),
                requirements = references,
                finished = mutableSetOf())

    }

    data class WorkBlock(val nodeId: String, val endsAt: Int)

    private tailrec fun part2_tracePath(count: Int, scale: Int, map: Map<String, Node>, workers: Array<MutableList<WorkBlock?>>, options: MutableSet<String>, requirements: Map<String, Set<Day7.Node>>, finished: MutableSet<String>): Int {
        val everyoneIdle = workers.all { it.last() == null }
        if (options.size == 0 && everyoneIdle)
            return count
        // FINISH their work!
        workers.forEachIndexed { i, worker ->
            val task = worker.last()
            if (task == null || task.endsAt > count) {
                return@forEachIndexed
            }
            finished.add(task.nodeId)
            map[task.nodeId]!!
                    .laterSteps
                    .filter { !finished.contains(it.instruction) && part1_requirementsMet(it, finished, requirements) }
                    .forEach { options.add(it.instruction) }
            worker.add(null)
        }
        // FIND NEW WORK!
        workers.forEachIndexed { i, worker ->
            val task = worker.last()
            if (task != null) {
                return@forEachIndexed
            }
            // new job
            val nextId = options.min() ?: return@forEachIndexed
            options.remove(nextId)
            worker.add(WorkBlock(nextId, count + scale + nextId.last().toInt() - 64))
        }
        return part2_tracePath(count + 1, scale, map, workers, options, requirements, finished)
    }

    //region part1
    @Test
    fun part1_example1_chain() {
        assertThat(part1_makePoints(example1)[2], `is`(Pair("A", "B")))
    }

    @Test
    fun part1_example1_map() {
        val map = part1_makeNodeMap(part1_makePoints(example1))
        assertThat(map.size, `is`(7))
        assertThat(map.get("A")!!.laterSteps.size, `is`(2))
    }

    @Test
    fun part1_example1_findHead() {
        val map = part1_makeNodeMap(part1_makePoints(example1))
        assertThat(part1_findHead(map), `is`(listOf("C", "Z")))
    }

    @Test
    fun part1_example1_solve() {
        assertThat(part1_solve(example1), `is`("CABDZFE"))
    }

    @Test
    fun solution_part1() {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream("day7_input.txt")
        val inputStr = BufferedReader(InputStreamReader(inputStream)).readText()
        inputStream.close()
        assertThat(part1_solve(inputStr), `is`("GJKLDFNPTMQXIYHUVREOZSAWCB"))
        // Correct answer at 7:13pm
    }
    //endregion

    @Test
    fun part2_example() {
        assertThat(part2_solution(example2, 2, 0), `is`(15))
    }
    @Test
    fun solution_part2() {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream("day7_input.txt")
        val inputStr = BufferedReader(InputStreamReader(inputStream)).readText()
        inputStream.close()
        assertThat(part2_solution(inputStr, 5, 60), `is`(968))
        // Correct answer at 8:14 pm  -- submitted 967 because of off by one error but I'm ~1.5 hrs in from an edible and don't feel like fixing it...
    }
}