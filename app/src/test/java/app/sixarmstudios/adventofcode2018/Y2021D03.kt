package app.sixarmstudios.adventofcode2018

import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.io.BufferedReader
import java.io.InputStreamReader

class Y2021D03 {
    @Test
    fun part1_sample() {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream("y2021_d03_sample.txt")
        val inputStr = BufferedReader(InputStreamReader(inputStream)).readText()
                .split("\n")
                .filter { it.length > 1 }
        assertThat("1st good", calcDer(inputStr, 0) == 1)
        assertThat("2nd good", calcDer(inputStr, 1) == 0)
        assertThat("2nd good", calcDer(inputStr, 2) == 1)
        assertThat("2nd good", calcDer(inputStr, 3) == 1)
        assertThat("2nd good", calcDer(inputStr, 4) == 0)
        assertThat("Number is good", calcNumber(inputStr) == 22)
        assertThat("1st good", calcDer2(inputStr, 0) == 0)
        assertThat("2nd good", calcDer2(inputStr, 1) == 1)
        assertThat("2nd good", calcDer2(inputStr, 2) == 0)
        assertThat("2nd good", calcDer2(inputStr, 3) == 0)
        assertThat("2nd good", calcDer2(inputStr, 4) == 1)
        assertThat("Number is good", calcNumber2(inputStr) == 9)
        assertThat("all good", calcNumber(inputStr) * calcNumber2(inputStr) == 198)
    }

    @Test
    fun part1_mine() {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream("y2021_d03_mine.txt")
        val inputStr = BufferedReader(InputStreamReader(inputStream)).readText()
                .split("\n")
                .filter { it.length > 1 }
        println(calcNumber(inputStr) * calcNumber2(inputStr))
        assertThat("all good", calcNumber(inputStr) * calcNumber2(inputStr) == 198)
    }


    @Test
    fun part1_mine_rstecker() {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream("y2021_d03_mine_rstecker.txt")
        val inputStr = BufferedReader(InputStreamReader(inputStream)).readText()
                .split("\n")
                .filter { it.length > 1 }
        println(calcNumber(inputStr) * calcNumber2(inputStr))
        assertThat("all good", calcNumber(inputStr) * calcNumber2(inputStr) == 2640986)
    }

    private fun calcNumber(inputStr: List<String>): Int {
        val thing = inputStr[0].mapIndexed { index, c ->
            calcDer(inputStr, index)
        }
        return thing.joinToString("").toInt(2)
    }

    private fun calcDer(inputStr: List<String>, index: Int): Int {
        return inputStr.map { it.get(index) }.toList().let {
            val count = it.sumBy { c -> c.toString().toInt() }
            return@let if (count > (it.size - count)) 1 else 0
        }
    }

    private fun calcNumber2(inputStr: List<String>): Int {
        val thing = inputStr[0].mapIndexed { index, c ->
            calcDer2(inputStr, index)
        }
        return thing.joinToString("").toInt(2)
    }

    private fun calcDer2(inputStr: List<String>, index: Int): Int {
        return inputStr.map { it.get(index) }.toList().let {
            val count = it.sumBy { c -> c.toString().toInt() }
            return@let if (count < (it.size - count)) 1 else 0
        }
    }


    @Test
    fun part2_sample() {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream("y2021_d03_sample.txt")
        val inputStr = BufferedReader(InputStreamReader(inputStream)).readText()
                .split("\n")
                .filter { it.length > 1 }
        assertThat("shark", calcDer3(inputStr, 0, 1) == 23)
        assertThat("shark", calcDer3(inputStr, 0, 0) == 10)
        assertThat("winter", calcDer3(inputStr, 0, 0) * calcDer3(inputStr, 0, 1) == 230)
    }


    @Test
    fun part2_mine() {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream("y2021_d03_mine.txt")
        val inputStr = BufferedReader(InputStreamReader(inputStream)).readText()
                .split("\n")
                .filter { it.length > 1 }
        println(calcDer3(inputStr, 0, 0) * calcDer3(inputStr, 0, 1))
        assertThat("winter", calcDer3(inputStr, 0, 0) * calcDer3(inputStr, 0, 1) == 230)
    }
    @Test
    fun part2_mine_rstecker() {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream("y2021_d03_mine_rstecker.txt")
        val inputStr = BufferedReader(InputStreamReader(inputStream)).readText()
                .split("\n")
                .filter { it.length > 1 }
        println(calcDer3(inputStr, 0, 0) * calcDer3(inputStr, 0, 1))
        assertThat("winter", calcDer3(inputStr, 0, 0) * calcDer3(inputStr, 0, 1) == 230)
    }


    private fun calcDer3(inputStr: List<String>, index: Int, target: Int): Int {
        if (inputStr.size == 1) {
            return inputStr[0].toInt(2)
        }
        return inputStr.map { it.get(index) }.toList().let {
            val count1 = it.sumBy { c -> c.toString().toInt() }
            val count0 = it.size - count1
            return when {
                target == 1 && count1 == count0 -> calcDer3(inputStr.filter { it[index].toString() == target.toString() }, index + 1, target)
                target == 1 && count1 > count0 -> calcDer3(inputStr.filter { it[index].toString() == "1" }, index + 1, target)
                target == 1 -> calcDer3(inputStr.filter { it[index].toString() == "0" }, index + 1, target)
                target == 0 && count1 == count0 -> calcDer3(inputStr.filter { it[index].toString() == target.toString() }, index + 1, target)
                target == 0 && count1 > count0 -> calcDer3(inputStr.filter { it[index].toString() == "0" }, index + 1, target)
                else -> calcDer3(inputStr.filter { it[index].toString() == "1" }, index + 1, target)
            }
        }
    }

    private fun calcDer3(inputStr: List<String>, index: Int, tieBreaker: String): Int {
        if (inputStr.size == 1) {
            return inputStr[0].toInt(2)
        }
        return inputStr.map { it.get(index) }.toList().let {
            val count1 = it.sumBy { c -> c.toString().toInt() }
            val count0 = it.size - count1
            return when {
                count1 == count0 -> calcDer3(inputStr.filter { it[index].toString() == tieBreaker }, index + 1, tieBreaker)
                count1 > count0 -> calcDer3(inputStr.filter { it[index].toString() == "1" }, index + 1, tieBreaker)
                else -> calcDer3(inputStr.filter { it[index].toString() == "0" }, index + 1, tieBreaker)
            }
        }
    }

}