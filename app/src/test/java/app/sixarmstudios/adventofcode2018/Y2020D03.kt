package app.sixarmstudios.adventofcode2018

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.io.BufferedReader
import java.io.InputStreamReader

class Y2020D03 {
    data class Woods(val stepLeft: Long, val stepDown: Long, val pattern: List<String>) {
        private val width = pattern[0].length
        fun getStep(count: Long): Char? {
            val x = (stepLeft * count) % width
            val y = stepDown * count
            val row = pattern.getOrNull(y.toInt()) ?: return null
            val entry = row[x.toInt()]
//            val pretty = "${row.substring(0,x-1)}${if (entry == '#') 'X' else '0'}${row.substring(x+1, width)}"
//            System.out.println("Pulling  ||$pretty|| [$x,$y] from '$row' [${width}]")
            return entry
        }

        fun treeCount(): Long {
            return walk().filter { it == '#' }.size.toLong()
        }

        fun walk(): List<Char> {
            var i = 1L
            val result = mutableListOf<Char>()
            do {
                val c = getStep(i++)
                c?.let { result.add(it) }
            } while (c != null)
            return result
        }
    }

    @Test
    fun part1_sample() {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream("y2020_d03_sample.txt")
        val inputStr = BufferedReader(InputStreamReader(inputStream)).readText()
                .split("\n")
                .filter { it.isNotBlank() }
        inputStream.close()
        val w = Woods(3, 1, inputStr)
        assertThat('.', `is`(w.getStep(1)))
        assertThat('#', `is`(w.getStep(2)))
        assertThat(7, `is`(w.walk().filter { it == '#' }.size))
    }

    @Test
    fun part1_mine() {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream("y2020_d03_mine.txt")
        val inputStr = BufferedReader(InputStreamReader(inputStream)).readText()
                .split("\n")
                .filter { it.isNotBlank() }
        inputStream.close()
        val w = Woods(3, 1, inputStr)
        assertThat('#', `is`(w.getStep(1)))
        assertThat('#', `is`(w.getStep(2)))
        assertThat(299, `is`(w.walk().filter { it == '#' }.size))
    }


    @Test
    fun part2_sample() {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream("y2020_d03_sample.txt")
        val inputStr = BufferedReader(InputStreamReader(inputStream)).readText()
                .split("\n")
                .filter { it.isNotBlank() }
        inputStream.close()
        val w1 = Woods(1, 1, inputStr)
        val w2 = Woods(3, 1, inputStr)
        val w3 = Woods(5, 1, inputStr)
        val w4 = Woods(7, 1, inputStr)
        val w5 = Woods(1, 2, inputStr)
        assertThat(2, `is`(w1.treeCount()))
        assertThat(7, `is`(w2.treeCount()))
        assertThat(3, `is`(w3.treeCount()))
        assertThat(4, `is`(w4.treeCount()))
        assertThat(2, `is`(w5.treeCount()))
        assertThat(336, `is`(w1.treeCount() * w2.treeCount() * w3.treeCount() * w4.treeCount() * w5.treeCount()))
    }


    @Test
    fun part2_mine() {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream("y2020_d03_mine.txt")
        val inputStr = BufferedReader(InputStreamReader(inputStream)).readText()
                .split("\n")
                .filter { it.isNotBlank() }
        inputStream.close()
        val w1 = Woods(1, 1, inputStr)
        val w2 = Woods(3, 1, inputStr)
        val w3 = Woods(5, 1, inputStr)
        val w4 = Woods(7, 1, inputStr)
        val w5 = Woods(1, 2, inputStr)
        assertThat(67, `is`(w1.treeCount()))
        assertThat(299, `is`(w2.treeCount()))
        assertThat(67L, `is`(w3.treeCount()))
        assertThat(71L, `is`(w4.treeCount()))
        assertThat(38L, `is`(w5.treeCount()))
        val x = 67L * 299L * 67L * 71L * 67L
        // 2089930431 <- 1st try 8:05am is "too low"
        // 6384897727 <-- 2nd try 8:07am is too high
        assertThat(3621285278, `is`(w1.treeCount() * w2.treeCount() * w3.treeCount() * w4.treeCount() * w5.treeCount()))
    }
}