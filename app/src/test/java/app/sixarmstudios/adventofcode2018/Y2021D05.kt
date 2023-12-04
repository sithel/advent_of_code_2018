package app.sixarmstudios.adventofcode2018

import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.io.BufferedReader
import java.io.InputStreamReader

class Y2021D05 {

    @Test
    fun part1_sample() {
        val count = mapStuff("y2021_d05_sample.txt", false)

        println("Count: $count")
        assertThat("is good", count == 5)
    }


    @Test
    fun part1_mine() {
        val count = mapStuff("y2021_d05_mine.txt", false)

        println("Count: $count")
        assertThat("is good", count == 5)
    }

    @Test
    fun part2_sample() {
        val count = mapStuff("y2021_d05_sample.txt", true)

        println("Count: $count")
        assertThat("is good", count == 12)
    }

    @Test
    fun part2_mine() {
        val count = mapStuff("y2021_d05_mine.txt", true)

        println("Count: $count")
        assertThat("is good", count == 12)
    }


    fun mapStuff(fileName: String, includeDiagonal: Boolean): Int {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream(fileName)
        val lines = BufferedReader(InputStreamReader(inputStream)).readText()
                .split("\n")
                .filter { it.isNotBlank() }
                .map { line ->
                    val p = line
                            .split(" -> ")
                            .map { it.split(",") }
                            .flatten()
                            .map { it.trim().toInt() }
                    Line(Pair(p[0], p[1]), Pair(p[2], p[3]))
                }
        println(lines.map { "[H ${it.isHorizontal()} / V ${it.isVertical()}] : $it" }.joinToString("\n"))
        println("~~~~")
        val maxY = lines.map { it.maxY() }.max() ?: 0
        val maxX = lines.map { it.maxX() }.max() ?: 0
        val map = List(maxY + 1) { y -> MutableList(maxX + 1) { x -> "." } }
        lines.forEach { it.markMap(map, includeDiagonal) }
        (0..maxY).forEach { y ->
            (0..maxX).forEach { x ->
                print(map[y][x])
            }
            print("\n")
        }
        val count = map.sumBy { it.filter { it != "." && it != "1" }.count() }
        return count
    }

    data class Line(val start: Pair<Int, Int>, val end: Pair<Int, Int>) {
        fun maxX(): Int = start.first.coerceAtLeast(end.first)

        fun maxY(): Int = start.second.coerceAtLeast(end.second)

        fun markMap(map: List<MutableList<String>>, includeDiagonal: Boolean) {
            if (isHorizontal()) {
                if (start.first < end.first)
                    (start.first..end.first).forEach { layDownTrack(map, it, start.second) }
                else
                    (end.first..start.first).forEach { layDownTrack(map, it, start.second) }
            } else if (isVertical()) {
                if (start.second < end.second)
                    (start.second..end.second).forEach { layDownTrack(map, start.first, it) }
                else
                    (end.second..start.second).forEach { layDownTrack(map, start.first, it) }
            } else if (includeDiagonal) {
                val xDelta = if (start.first < end.first) 1 else -1
                val yDelta = if (start.second < end.second) 1 else -1
                var p = Pair(start.first, start.second)
                while (p != end) {
                    layDownTrack(map, p.first, p.second)
                    p = Pair(p.first + xDelta, p.second + yDelta)
                }
                layDownTrack(map, p.first, p.second)
            } else
                println("poop: $this")
        }

        fun layDownTrack(map: List<MutableList<String>>, x: Int, y: Int) {
            val existing = map[y][x]
            val newVal = if (existing == ".") 1 else existing.toInt() + 1
            map[y][x] = newVal.toString()
        }

        fun isHorizontal(): Boolean = start.second == end.second
        fun isVertical(): Boolean = start.first == end.first

    }
}