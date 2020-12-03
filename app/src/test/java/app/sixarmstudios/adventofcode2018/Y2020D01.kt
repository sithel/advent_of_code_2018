package app.sixarmstudios.adventofcode2018

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.io.BufferedReader
import java.io.InputStreamReader

class Y2020D01 {
    @Test
    fun sample_p1() {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream("y2020_d01_sample.txt")
        val inputStr = BufferedReader(InputStreamReader(inputStream)).readText().split("\n").filter { it.isNotBlank() }
        inputStream.close()
        System.out.println("I see : $inputStr")
        val data = inputStr.mapNotNull { System.out.println("whaaa? $it");it.toLong() }
        assertThat(1721L, `is`(data[0]))
        data.forEachIndexed { index, l ->
            System.out.println("Checking " + l)
            val result = data.subList(index + 1, data.size).find { l2 ->
                val result = l2 + l
                System.out.println("  + $l2 =\t $result")
                result == 2020L
            }
            if (result != null) {
                val munge = l * result
                System.out.println(" WE DONE! $l + $result -> 2020 ==> $munge")
                assertThat(514579, `is`(munge))
                return
            }
        }
    }

    @Test
    fun mine_1_p1() {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream("y2020_d01_mine.txt")
        val inputStr = BufferedReader(InputStreamReader(inputStream)).readText()
                .split("\n")
                .filter { it.isNotBlank() }
        inputStream.close()
        System.out.println("I see : $inputStr")
        val data = inputStr.map { it.toLong() }
        assertThat(1895L, `is`(data[0]))
        data.forEachIndexed { index, l ->
            val result = data.subList(index + 1, data.size).find { l2 ->
                val result = l2 + l
                result == 2020L
            }
            if (result != null) {
                val munge = l * result
                System.out.println(" WE DONE! $l + $result -> 2020 ==> $munge")
                assertThat(290784L, `is`(munge))
                return
            }
        }
    }


    @Test
    fun sample_p2() {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream("y2020_d01_sample.txt")
        val inputStr = BufferedReader(InputStreamReader(inputStream)).readText().split("\n").filter { it.isNotBlank() }
        inputStream.close()
        val data = inputStr.map { it.toLong() }
        assertThat(1721L, `is`(data[0]))
        data.forEachIndexed outer@{ index, l1 ->
            System.out.println("Checking " + l1)
            data.subList(index + 1, data.size)
                    .forEachIndexed mid@{ i2, l2 ->
                        val result = l2 + l1
                        if (result > 2020L)
                            return@mid
                        data.forEachIndexed inner@{ i3, l3 ->
                            val inResult = l1 + l2 + l3
                            if (inResult == 2020L) {
                                System.out.println("We done! $l1 + $l2 + $l3 = 2020")
                                val prod = l1 * l2 * l3
                                System.out.println("and our prod is $prod")
                                assertThat(prod, `is`(241861950L))
                                return@outer
                            }
                        }
                    }
        }
    }


    @Test
    fun mine_p2() {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream("y2020_d01_mine.txt")
        val inputStr = BufferedReader(InputStreamReader(inputStream)).readText().split("\n").filter { it.isNotBlank() }
        inputStream.close()
        val data = inputStr.map { it.toLong() }
        assertThat(1895L, `is`(data[0]))
        data.forEachIndexed outer@{ index, l1 ->
            System.out.println("Checking " + l1)
            data.subList(index + 1, data.size)
                    .forEachIndexed mid@{ i2, l2 ->
                        val result = l2 + l1
                        if (result > 2020L)
                            return@mid
                        data.forEachIndexed inner@{ i3, l3 ->
                            val inResult = l1 + l2 + l3
                            if (inResult == 2020L) {
                                System.out.println("We done! $l1 + $l2 + $l3 = 2020")
                                val prod = l1 * l2 * l3
                                System.out.println("and our prod is $prod")
                                assertThat(prod, `is`(177337980L))
                                return@outer
                            }
                        }
                    }
        }
    }
}