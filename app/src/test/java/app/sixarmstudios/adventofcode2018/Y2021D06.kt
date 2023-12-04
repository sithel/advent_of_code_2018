package app.sixarmstudios.adventofcode2018

import junit.framework.Assert.assertEquals
import org.hamcrest.MatcherAssert
import org.junit.Test
import java.io.BufferedReader
import java.io.InputStreamReader

class Y2021D06 {
    @Test
    fun part1_sample() {
        assertEquals(26, fishStuff("y2021_d06_sample.txt", 18))
        assertEquals(5934, fishStuff("y2021_d06_sample.txt", 80))
        assertEquals(5934, fishStuff("y2021_d06_sample.txt", 80))
    }
    @Test
    fun part1_mine() {
        assertEquals(365862, fishStuff("y2021_d06_mine.txt", 80))
    }

    @Test
    fun part2_sample() {
        assertEquals(26, fishStuff2("y2021_d06_sample.txt", 18))
        assertEquals(5934, fishStuff2("y2021_d06_sample.txt", 80))
        assertEquals(26984457539L, fishStuff2("y2021_d06_sample.txt", 256))
    }
    @Test
    fun part2_mine() {
        assertEquals(351188, fishStuff2("y2021_d06_mine.txt", 80))
        assertEquals(365862, fishStuff2("y2021_d06_mine.txt", 256))
    }

    fun fishStuff2(fileName: String, days: Int): Long {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream(fileName)
        val school = MutableList(BIRTH_AGE+1){ 0L }

        val fish = BufferedReader(InputStreamReader(inputStream)).readText()
                .split("\n")
                .filter { it.isNotBlank() }
                .map { it.split(",").filter { it.isNotBlank() } }
                .flatten()
                .map { it.toLong() }
                .forEachIndexed { i, f -> school[f.toInt()]++ }
        println(school)
        (1..days).forEach {
            ageSchool(school)
        }
        return school.sum()
    }

    private fun ageSchool(school: MutableList<Long>) {
        val births = school[0]
        (1 .. BIRTH_AGE).forEach { i->
            school[i - 1] = school[i]
        }
        school[BIRTH_AGE] = births
        school[RESET_AGE] += births
    }

    fun fishStuff(fileName: String, days: Int): Int {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream(fileName)
        val fish = BufferedReader(InputStreamReader(inputStream)).readText()
                .split("\n")
                .filter { it.isNotBlank() }
                .map { it.split(",").filter { it.isNotBlank() } }
                .flatten()
                .map { it.toInt() }
                .toMutableList()
        (1..days).forEach {
            ageFish(fish)
        }
        return fish.size
    }

    /*
Initial state: 3,4,3,1,2
After  1 day:  2,3,2,0,1
After  2 days: 1,2,1,6,0,8
After  3 days: 0,1,0,5,6,7,8
After  4 days: 6,0,6,4,5,6,7,8,8
After  5 days: 5,6,5,3,4,5,6,7,7,8
After  6 days: 4,5,4,2,3,4,5,6,6,7
After  7 days: 3,4,3,1,2,3,4,5,5,6
After  8 days: 2,3,2,0,1,2,3,4,4,5
After  9 days: 1,2,1,6,0,1,2,3,3,4,8
After 10 days: 0,1,0,5,6,0,1,2,2,3,7,8
After 11 days: 6,0,6,4,5,6,0,1,1,2,6,7,8,8,8
After 12 days: 5,6,5,3,4,5,6,0,0,1,5,6,7,7,7,8,8
After 13 days: 4,5,4,2,3,4,5,6,6,0,4,5,6,6,6,7,7,8,8
After 14 days: 3,4,3,1,2,3,4,5,5,6,3,4,5,5,5,6,6,7,7,8
After 15 days: 2,3,2,0,1,2,3,4,4,5,2,3,4,4,4,5,5,6,6,7
After 16 days: 1,2,1,6,0,1,2,3,3,4,1,2,3,3,3,4,4,5,5,6,8
After 17 days: 0,1,0,5,6,0,1,2,2,3,0,1,2,2,2,3,3,4,4,5,7,8
After 18 days: 6,0,6,4,5,6,0,1,1,2,6,0,1,1,1,2,2,3,3,4,6,7,8,8,8,8
     */

    private fun ageFish(fish: MutableList<Int>) {
        fish.forEachIndexed { i, _ ->  fish[i] = fish[i] - 1 }
        val newFish = fish.count{ it < 0}
        fish.forEachIndexed { i, _ -> if (fish[i] < 0) fish[i] = RESET_AGE }
        (0 until newFish).forEach { fish.add(BIRTH_AGE) }
    }
    companion object {
        const val RESET_AGE = 6
        const val BIRTH_AGE = 8
    }
}