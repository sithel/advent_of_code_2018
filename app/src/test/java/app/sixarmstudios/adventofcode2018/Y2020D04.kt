package app.sixarmstudios.adventofcode2018

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.io.BufferedReader
import java.io.InputStreamReader

class Y2020D04 {
    @Test
    fun part1_sample() {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream("y2020_d04_sample.txt")
        val inputStr = BufferedReader(InputStreamReader(inputStream)).readText()
                .split("\n\n")

        val data = inputStr
                .map {
                    it.replace("\n", " ")
                            .split(" ")
                            .filter { it.isNotEmpty() }
                            .map { it.substring(0, 3) }
                }
                .map { it.toSet() }
                .filter { p ->
                    System.out.println(p)
                    Field.values().filter { it.required }.all { it.entry in p }
                }
        System.out.println(data)
        assertThat(2, `is`(data.size))
    }

    @Test
    fun part1_mine() {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream("y2020_d04_mine.kt")
        val inputStr = BufferedReader(InputStreamReader(inputStream)).readText()
                .split("\n\n")

        val data = inputStr
                .map {
                    it.replace("\n", " ")
                            .split(" ")
                            .filter { it.isNotEmpty() }
                            .map { it.substring(0, 3) }
                }
                .map { it.toSet() }
                .filter { p ->
                    System.out.println(p)
                    Field.values().filter { it.required }.all { it.entry in p }
                }
        System.out.println(data)
        assertThat(245, `is`(data.size))
    }


    enum class Field(val entry: String, val required: Boolean) {
        BIRTH_YEAR("byr", true),
        ISSUE("iyr", true),
        EXPR("eyr", true),
        HEIGHT("hgt", true),
        HAIR("hcl", true),
        EYE("ecl", true),
        PASS("pid", true),
        COUNTRY("cid", false)
    }
    /*
    byr (Birth Year)
iyr (Issue Year)
eyr (Expiration Year)
hgt (Height)
hcl (Hair Color)
ecl (Eye Color)
pid (Passport ID)
cid (Country ID)
     */
}