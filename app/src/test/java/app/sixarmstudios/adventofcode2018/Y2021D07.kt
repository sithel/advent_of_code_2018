package app.sixarmstudios.adventofcode2018

import junit.framework.Assert
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.math.abs

class Y2021D07 {

    @Test
    fun part1_sample() {
        assertEquals(37, fuel("y2021_d07_sample.txt", this::burnForConst))
    }

    @Test
    fun part1_mine() {
        assertEquals(356922, fuel("y2021_d07_mine.txt", this::burnForConst))
    }

    @Test
    fun part2_sample() {
        assertEquals(168, fuel2("y2021_d07_sample.txt", this::burnForWeight))
    }

    // try 1 206217285 : too high!
    /*
~~~ + ends at 490 / 212088689
~~~ - ends at 361 / 206217285
                  / 206217285
     */
    @Test
    fun part2_mine() {
        assertEquals(168, fuel("y2021_d07_mine.txt", this::burnForWeight))
    }


    fun fuel(fileName: String, burnFor:(crabs: List<Int>,target:Int) -> Int): Int {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream(fileName)
        val crabs = BufferedReader(InputStreamReader(inputStream)).readText()
                .split("\n")
                .filter { it.isNotBlank() }
                .map { it.split(",").filter { it.isNotBlank() } }
                .flatten()
                .map { it.toInt() }
        println(crabs)
        val dist = mutableMapOf<Int, Int>()
        crabs.forEach { dist.put(it, dist.getOrDefault(it, 0) + 1) }
        println("avg : ${crabs.average()} / min : ${crabs.min()} / max : ${crabs.max()} / dist: $dist")
        var curTarget = crabs.average().toInt()
        var curCost = burnFor(crabs,curTarget)

        var nextTarget = curTarget + 1
        var nextCost = burnFor(crabs, nextTarget)
        while (curCost > nextCost) {
            curTarget = nextTarget
            curCost = nextCost
            nextTarget++
            nextCost = burnFor(crabs, nextTarget)
        }
        println("~~~ + ends at $curTarget / $curCost")
        val posCost =curCost

        nextTarget = crabs.average().toInt() - 1
        nextCost = burnFor(crabs, nextTarget)
        while (curCost > nextCost) {
            curTarget = nextTarget
            curCost = nextCost
            nextTarget--
            nextCost = burnFor(crabs, nextTarget)
        }
        println("~~~ - ends at $curTarget / $curCost")
        return Math.min(curCost, posCost).toInt()
    }



    fun fuel2(fileName: String, burnFor:(crabs: List<Int>,target:Int) -> Int): Int {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream(fileName)
        val crabs = BufferedReader(InputStreamReader(inputStream)).readText()
                .split("\n")
                .filter { it.isNotBlank() }
                .map { it.split(",").filter { it.isNotBlank() } }
                .flatten()
                .map { it.toInt() }
        println(crabs)
        val dist = mutableMapOf<Int, Int>()
        crabs.forEach { dist.put(it, dist.getOrDefault(it, 0) + 1) }
        println("avg : ${crabs.average()} / min : ${crabs.min()} / max : ${crabs.max()} / dist: $dist")
        var curTarget = crabs.average().toInt()
        var curCost = burnFor(crabs,curTarget)
        ((crabs.min()?:1) .. (crabs.max()?:1)).forEach {
            val cost = burnFor(crabs, it)
            if (cost < curCost) {
                curCost = cost
                curTarget = it
            }
        }
        println("~~~ ends at $curTarget / $curCost")
        return curCost
    }

    private fun burnForConst(crabs: List<Int>, target: Int): Int {
        return crabs.map { Math.abs(it - target) }.sum()
    }

    private fun burnForWeight(crabs: List<Int>, target: Int): Int {
        return crabs.map {
            val dist = abs(it - target)
            val fuel = (dist * (dist+1)).div(2)
            return@map fuel
        }.sum()
    }
}