package app.sixarmstudios.adventofcode2018

import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.regex.MatchResult
import java.util.regex.Pattern

class Y2023D01 {
  private fun chewThroughProb1(fileName:String) :Int{
    val inputStream = this.javaClass.classLoader!!.getResourceAsStream(fileName)
    return BufferedReader(InputStreamReader(inputStream)).readText()
      .split("\n")
      .filter { it.isNotBlank() }
      .map { s ->
        val data = s.split("")
          .filter { it.isNotBlank() }
          .mapNotNull { try {
            Integer.parseInt(it)
          } catch (e:Exception)  {
            null
          }}
        val first = data.first()
        val last = data.last()
        val num = first * 10 + last
        println("I see: $num")
        num
      }
      .sum()
  }
  val pattern = Pattern.compile("((one)|(two)|(three)|(four)|(five)|(six)|(seven)|(eight)|(nine)|(ten)|(zero))")
  private fun scrubEnglish(s:String):String {
    var curS = s
    var m = pattern.matcher(curS)
    var index = 0
    while(m.find(index)) {
      val v = m.group()
      val num = v.replace("one","1")
        .replace("two","2")
        .replace("three","3")
        .replace("four","4")
        .replace("five","5")
        .replace("six","6")
        .replace("seven","7")
        .replace("eight","8")
        .replace("nine","9")
      curS = curS.subSequence(0, m.start()).toString() + num + curS.subSequence(m.start(), curS.length)
      index = m.start() + 2
      m = pattern.matcher(curS)
//      println("I found '$v' --> $curS")
    }
    return curS
  }
  private fun chewThroughProb2(fileName:String) :Int{
    val inputStream = this.javaClass.classLoader!!.getResourceAsStream(fileName)
    return BufferedReader(InputStreamReader(inputStream)).readText()
      .split("\n")
      .filter { it.isNotBlank() }
      .map { origS ->
        val s = scrubEnglish(origS)
        val data = s.split("")
          .filter { it.isNotBlank() }
          .mapNotNull { try {
            Integer.parseInt(it)
          } catch (e:Exception)  {
            null
          }}
        val first = data.first()
        val last = data.last()
        val num = first * 10 + last
        println(">> I see: $num       [$origS]   ->    [$s]")
        num
      }
      .sum()
  }
  @Test
  fun prob1_sample() {
    assert(true)
    val result = chewThroughProb1("y2023_d01.sample.txt")
    println("I see rows equaling $result")
    assertEquals(142, result)
  }
  @Test
  fun prob1_mine() {
    assert(true)
    val result = chewThroughProb1("y2023_d01.mine.txt")
    println("I see rows equaling $result")
    assertEquals(54450, result)
  }
  @Test
  fun prob2_sample() {
    assert(true)
    val result = chewThroughProb2("y2023_d01.sample.2.txt")
    println("I see rows equaling $result")
    assertEquals(281, result)
  }
  @Test
  fun prob2_mine() {
    assert(true)
    val result = chewThroughProb2("y2023_d01.mine.txt")
    println("I see rows equaling $result")
//    assertEquals(54258, result)  // 1st guess: 54258 : That's not the right answer; your answer is too low.
//    assertEquals(54216, result)  // 2nd guess: 54216 : That's not the right answer; your answer is too low.
    assertEquals(0, result)  //3rd
  }
}