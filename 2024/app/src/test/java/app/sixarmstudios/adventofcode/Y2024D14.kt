package app.sixarmstudios.adventofcode

import junit.framework.TestCase.assertEquals
import org.junit.Test

class Y2024D14 : Shark() {

  data class Map(val robots: List<Robot>, val w: Int = 101, val h: Int = 103) {
    fun tick() {
      robots.forEach { it.move(this) }
    }

    fun roboCount(quad: Int): Long = robots.filter { it.calcQuad(this) == quad }.count().toLong()

    fun safetyFactor(): Long {
      return roboCount(1) * roboCount(2) * roboCount(3) * roboCount(4)
    }
  }

  data class Robot(var x: Int, var y: Int, val dx: Int, val dy: Int) {
    override fun toString(): String = "[R @ $x,$y ~~ $dx,$dy]"
    fun move(map: Map) {
      val newX = (x + dx + map.w) % map.w
      val newY = (y + dy + map.h) % map.h
      x = newX
      y = newY
    }

    fun calcQuad(map: Map): Int {
      return when {
        this.x < map.w / 2 && this.y < map.h / 2 -> 1
        this.x > map.w / 2 && this.y < map.h / 2 -> 2
        this.x < map.w / 2 && this.y > map.h / 2 -> 3
        this.x > map.w / 2 && this.y > map.h / 2 -> 4
        else -> {
          println("ignoring $this as it's in the middle of the map")
          0
        }
      }
    }
  }

  //p=0,4 v=3,-3
  val regex = Regex("""(?m)p=(\d+),(\d+) v=(-?\d+),(-?\d+)""")

  fun solution1(fileName: String, width: Int, height: Int): Long {
    val map = readFile(fileName, width, height)

    (0 until 100).forEach {
      map.tick()
    }
    return map.safetyFactor()
  }

  fun solution2(fileName: String): Long = 0

  fun readFile(fileName: String, width: Int, height: Int): Map {
    val matches = regex.findAll(loadFile(fileName))
    return Map(
      w = width,
      h = height,
      robots = matches
        .map { match ->
          val g = match.groupValues
          Robot(
            x = g[1].toInt(),
            y = g[2].toInt(),
            dx = g[3].toInt(),
            dy = g[4].toInt()
          )
        }
        .toList()
    )
  }

  @Test
  fun example1() {
    val fileName = "y2024_d14_example.txt"
    assertEquals(12, solution1(fileName, 11, 7))
  }

  @Test
  fun mine1() {
    val fileName = "y2024_d14_mine.txt"
    assertEquals(-1, solution1(fileName, 101, 103))
    // 228841008 - too high (had w/h flipped on input!)
    // 228531240 - too high ( boooo! was doing 0 .. 100, needed to be 0 until 100)
    // 215476074
  }

  @Test
  fun example2() {
    assertEquals(-1, solution2("y2024_d14_example.txt"))
  }

  @Test
  fun mine2() {
    assertEquals(-1, solution2("y2024_d14_mine.txt"))
  }
}