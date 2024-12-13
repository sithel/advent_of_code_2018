package app.sixarmstudios.adventofcode

import app.sixarmstudios.adventofcode.Y2024D07.Equation
import junit.framework.TestCase.assertEquals
import org.junit.Test
import kotlin.math.exp

class Y2024D12 : Shark() {

  fun solution1(fileName: String): Long {
    val farm = buildFarm(fileName)

    return exploreFarm(farm).plot
      .map { it.calcPrice() }
      .sum()
  }

  fun solution2(fileName: String): Long {
    val farm = buildFarm(fileName)

    return exploreFarm(farm).plot
      .map { it.calcPrice2() }
      .sum()
  }

  data class P(val row: Int, val col: Int) {
    val right: P by lazy { P(row, col + 1) }
    val down: P by lazy { P(row + 1, col) }
    val left: P by lazy { P(row, col - 1) }
    val up: P by lazy { P(row - 1, col) }
    override fun toString(): String = "P($row,$col)"
  }

  data class Plot(val entries: MutableSet<P>, val type: Char, val edges: MutableSet<P>) {
    val oldEdges: MutableSet<P> = mutableSetOf()
    fun nextEdge(): P? = try {
      edges.first()
        .also { oldEdges.add(it) }
        .also { edges.remove(it) }
    } catch (_: Exception) {
      null
    }

    fun calcPrice(): Long {
      val edges = entries.map { n ->
        var r = 0
        if (n.left.let { !entries.contains(it) })
          ++r
        if (n.down.let { !entries.contains(it) })
          ++r
        if (n.right.let { !entries.contains(it) })
          ++r
        if (n.up.let { !entries.contains(it) })
          ++r
        r
      }.sum()
      val v = entries.size.toLong() * edges
      println(">>>> ${entries.size} x $edges [was ${oldEdges.size}] = $v")
      return v
    }

    fun calcPrice2(): Long {
      val edges = oldEdges.fold(mutableSetOf<P>()) { acc, n ->
        if(!entries.contains(n))
          acc.add(n)
//        n.left.let { if(!entries.contains(it) && !oldEdges.contains(it)) { acc.add(it) }}
//        n.right.let { if(!entries.contains(it) && !oldEdges.contains(it)) { acc.add(it) }}
//        n.up.let { if(!entries.contains(it) && !oldEdges.contains(it)) { acc.add(it) }}
//        n.down.let { if(!entries.contains(it) && !oldEdges.contains(it)) { acc.add(it) }}
        acc
      }
      val left_right = edges.map { it.col }.toSet().let{
        println("  > ${it.joinToString(", ")}")
        it
      }.size
      val up_down = edges.map { it.row }.toSet().let{
        println("  > ${it.joinToString(", ")}")
        it
      }.size
      val s = left_right + up_down
      val v = entries.size.toLong() * s
      println(">>>> ${entries.size} x $s [l/r = $left_right, u/d = $up_down]" +
        "[was ${oldEdges.size} / ${edges.size}] = $v\t\t\t[${edges.joinToString(", ")}]")
      return v
    }

    fun report() {
      oldEdges
      println("\t\t>> Plot $type [area: ${entries.size}, perimeter: ${oldEdges.size}]")
      val rowMax = (oldEdges.maxByOrNull { it.row }?.row ?: 0) + 3
      val colMax = (oldEdges.maxByOrNull { it.col }?.col ?: 0) + 3
      val blankMap = (0..rowMax).map { (0..colMax).map { ' ' }.toMutableList() }
      oldEdges.forEach {
        val v = if (entries.contains(it)) {
          type
        } else {
          'x'
        }
        blankMap[it.row + 1][it.col + 1] = v
      }
//      realEdges.forEach { blankMap[it.row][it.col] = 'x' }
      println(blankMap.map { it.joinToString("") }.joinToString("\n"))
      println("Entries: [${entries.joinToString(", ")}]")
      println("Edges: [${oldEdges.joinToString(", ")}]")
      calcPrice()
    }
  }

  data class Farm(
    val map: List<List<Char>>,
    val plot: MutableSet<Plot>,
    val plotted: MutableSet<P>,
    val unknown: MutableSet<P>
  ) {
    fun get(p: P): Char = map[p.row][p.col]
    fun safeGet(p: P): Char? = map.getOrNull(p.row)?.getOrNull(p.col)
    fun shouldExplore(p: P): P? = if (plotted.contains(p)) null else p
    fun nextUnkown(): P = try {
      unknown.first()
        .also { unknown.remove(it) }
    } catch (e: Exception) {
      throw IllegalStateException("Hoooooow did you manage to fuck this up?")
    }
  }

  fun buildFarm(fileName: String): Farm {
    return Farm(
      map = processFileData(loadFile(fileName))
        .map { it.split("").filter { it.isNotBlank() }.map { it[0] } },
      plot = mutableSetOf(),
      plotted = mutableSetOf(),
      unknown = mutableSetOf())
  }

  /**
   * If the start is:
   *   - invalid : expand via next edge or return Farm
   *   -
   */
  fun expandPlot(start: P, farm: Farm, plot: Plot): Plot {
    val exploreTo = if (farm.plotted.contains(start)) null else farm.safeGet(start)
//    println("              > Exploring @ $start [${exploreTo}] " +
//      "(${farm.unknown.size} remaining / ${farm.plotted.size} explored)")
    when (exploreTo) {
      null -> {}
      plot.type -> {
        farm.unknown.remove(start)
        plot.entries.add(start)
        farm.plotted.add(start)
        farm.shouldExplore(start.right)?.also { plot.edges.add(it) }
        farm.shouldExplore(start.down)?.also { plot.edges.add(it) }
        farm.shouldExplore(start.left)?.also { plot.edges.add(it) }
        farm.shouldExplore(start.up)?.also { plot.edges.add(it) }
      }

      else -> {
        farm.unknown.add(start)
      }
    }
//    println("Edges: ${plot.edges.joinToString(", ")}\t\t PLOTTED: ${farm.plotted.joinToString(", ")}")
    val nextEdge = plot.nextEdge() ?: return plot
    return expandPlot(nextEdge, farm, plot)
  }

  fun buildPlot(start: P, farm: Farm): Plot {
    var newPlot = Plot(entries = mutableSetOf(start),
      type = farm.get(start),
      edges = mutableSetOf(start.right, start.down, start.left, start.up)
    )
    println("   > Starting plot @ $start [${farm.safeGet(start)}] ")
    farm.plot.add(newPlot)
    farm.plotted.add(start)
    return expandPlot(newPlot.nextEdge()!!, farm, newPlot)
  }

  fun exploreFarm(farm: Farm): Farm {
    buildPlot(P(0, 0), farm).also { it.report() }
    while (farm.unknown.isNotEmpty()) {
      val nextStart = farm.nextUnkown()
      println("         > Exploring farm @ $nextStart [${farm.safeGet(nextStart)}] " +
        "(${farm.unknown.size} remaining / ${farm.plotted.size} explored)\n\t\t" +
        "unknown: [${farm.unknown.joinToString(", ")}]" +
        "\n\t\tplotted: ${farm.plotted.joinToString(", ")}")
      buildPlot(nextStart, farm).also { it.report() }
    }
    return farm
  }

  @Test
  fun example1() {
    val fileName = "y2024_d12_example.txt"
    assertEquals(1930, solution1(fileName))
  }

  @Test
  fun mine1() {
    val fileName = "y2024_d12_mine.txt"
    assertEquals(1464678, solution1(fileName))
    // 1216 - too low
  }

  @Test
  fun example2() {
    assertEquals(1206, solution2("y2024_d12_example.txt"))
  }

  @Test
  fun mine2() {
    assertEquals(-1, solution1("y2024_d12_mine.txt"))
  }
}