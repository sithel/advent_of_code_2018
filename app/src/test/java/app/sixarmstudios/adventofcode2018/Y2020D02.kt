package app.sixarmstudios.adventofcode2018

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.io.BufferedReader
import java.io.InputStreamReader

class Y2020D02 {
    data class Entry(val min: Int, val max: Int, val letter: Char, val password: String) {
        fun isValid(): Boolean {
            val count = password.filter { it == letter }
            return count.length in min..max
        }
        fun isValid_v2(): Boolean {
            val p1 = password.getOrNull(min - 1)
            val p2 = password.getOrNull(max - 1)
            return when {
//                p1 == null || p2 == null -> false
                p1 == p2 -> false
                p1 == letter || p2 == letter -> true
                else -> false
            }
        }
        fun passLogic(): String {
            val p1 = password.getOrNull(min - 1)
            val p2 = password.getOrNull(max - 1)
            return "Expecting [$letter] : options ($p1, $p2) from $password \t(is ${password.length} and we're looking at $min/$max)"
        }
    }

    /**
     * sample: '2-9 c: ccccccccc'
     */
    val rule = Regex("(\\d+)-(\\d+) (\\w): (\\w+)")

    @Test
    fun part1_sample() {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream("y2020_d02_sample.txt")
        val inputStr = BufferedReader(InputStreamReader(inputStream)).readText()
                .split("\n")
                .filter { it.isNotBlank() }
        inputStream.close()
        val data = inputStr.mapNotNull {
            val group = rule.find(it) ?: return@mapNotNull null
            Entry(
                    min = group.groupValues[1].toInt(),
                    max = group.groupValues[2].toInt(),
                    letter = group.groupValues[3][0],
                    password = group.groupValues[4]
            )
        }
        System.out.println(data)
        assertThat(data[0], `is`(Entry(1, 3, 'a', "abcde")))
        val count = data.filter { it.isValid() }.size
        assertThat(count, `is`(2))
    }

    @Test
    fun part1_mine() {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream("y2020_d02_mine.txt")
        val inputStr = BufferedReader(InputStreamReader(inputStream)).readText()
                .split("\n")
                .filter { it.isNotBlank() }
        inputStream.close()
        val data = inputStr.mapNotNull {
            val group = rule.find(it) ?: return@mapNotNull null
            Entry(
                    min = group.groupValues[1].toInt(),
                    max = group.groupValues[2].toInt(),
                    letter = group.groupValues[3][0],
                    password = group.groupValues[4]
            )
        }
        System.out.println(data)
        assertThat(data[0], `is`(Entry(16, 18, 'h', "hhhhhhhhhhhhhhhhhh")))
        val count = data.filter { it.isValid() }.size
        assertThat(count, `is`(465))
    }


    @Test
    fun part2_sample() {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream("y2020_d02_sample.txt")
        val inputStr = BufferedReader(InputStreamReader(inputStream)).readText()
                .split("\n")
                .filter { it.isNotBlank() }
        inputStream.close()
        val data = inputStr.mapNotNull {
            val group = rule.find(it) ?: return@mapNotNull null
            Entry(
                    min = group.groupValues[1].toInt(),
                    max = group.groupValues[2].toInt(),
                    letter = group.groupValues[3][0],
                    password = group.groupValues[4]
            )
        }
        System.out.println(data)
        assertThat(data[0], `is`(Entry(1, 3, 'a', "abcde")))
        val count = data.filter { it.isValid_v2() }.size
        assertThat(count, `is`(1))
    }


    @Test
    fun part2_mine() {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream("y2020_d02_mine.txt")
        val inputStr = BufferedReader(InputStreamReader(inputStream)).readText()
                .split("\n")
                .filter { it.isNotBlank() }
        inputStream.close()
        val data = inputStr.mapNotNull {
            val group = rule.find(it) ?: return@mapNotNull null
            Entry(
                    min = group.groupValues[1].toInt(),
                    max = group.groupValues[2].toInt(),
                    letter = group.groupValues[3][0],
                    password = group.groupValues[4]
            )
        }
        System.out.println(data)
        assertThat(data[0], `is`(Entry(16, 18, 'h', "hhhhhhhhhhhhhhhhhh")))
        val count = data.filter { it.isValid_v2() }
        count.forEach { System.out.println(it.passLogic()) }
        // 416 <- 1st try "too high"
        // 242 <- 2nd try  "too low"
        assertThat(count.size, `is`(0))
    }

}