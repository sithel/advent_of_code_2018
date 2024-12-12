package app.sixarmstudios.adventofcode

import app.sixarmstudios.adventofcode.Y2024D07.Equation
import junit.framework.TestCase.assertEquals
import org.junit.Test
import kotlin.math.exp

class Y2024D12 : Shark() {

  fun solution1(fileName: String): Int = 0
  fun solution2(fileName: String): Int = 0

  data class P(val row: Int, val col: Int) {
    val right: P by lazy { P(row, col + 1) }
    val down: P by lazy { P(row + 1, col) }
    val left: P by lazy { P(row, col - 1) }
    val up: P by lazy { P(row - 1, col) }
  }

  data class Plot(val entries: MutableSet<P>, val type: Char, val edges: MutableSet<P>) {
    val oldEdges: MutableSet<P> = mutableSetOf()
    fun nextEdge(): P? = try {
      edges.first()
        .also { oldEdges.add(it) }
        .also { edges.remove(it) }
    } catch (e: Exception) {
      null
    }

    fun report() {
      val realEdges = oldEdges.filter { entries.contains(it) }
      println("Plot $type [area: ${entries.size}, perimeter: ${realEdges.size}]")
      if (realEdges.size < 6) {
        println("this is sus...")
        return
      }
      val rowMax = realEdges.maxByOrNull { it.row }?.row ?: 0
      val colMax = realEdges.maxByOrNull { it.col }?.col ?: 0
      val blankMap = (0..rowMax).map { (0..colMax).map { ' ' }.toMutableList() }
      entries.forEach { blankMap[it.row][it.col] = type }
//      realEdges.forEach { blankMap[it.row][it.col] = 'x' }
      println(blankMap.map { it.joinToString("") }.joinToString("\n"))
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
    fun shouldExplore(p: P): P? = if (plotted.contains(p) || unknown.contains(p)) null else p
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
    return expandPlot(newPlot.nextEdge()!!, farm, newPlot)
  }

  fun exploreFarm(farm: Farm): Farm {
    buildPlot(P(0, 0), farm).also { it.report() }
    while (farm.unknown.isNotEmpty()) {
      val nextStart = farm.nextUnkown()
      println("         > Exploring @ $nextStart [${farm.safeGet(nextStart)}] " +
        "(${farm.unknown.size} remaining / ${farm.plotted.size} explored)")
      buildPlot(nextStart, farm).also { it.report() }
    }
    return farm
  }

  @Test
  fun example1() {
    val fileName = "y2024_d12_example.txt"
    val farm = buildFarm(fileName)
//    val plot = buildPlot(P(0, 0), farm).plot.first()
//    println(" I see plot : $plot")
    println(" I see farm : $farm")
    println(" I see finished farm : ${exploreFarm(farm)}")

    assertEquals(1930, solution1(fileName))
  }

  @Test
  fun mine1() {
    val fileName = "y2024_d12_mine.txt"
    assertEquals(-1, solution1(fileName))
  }

  @Test
  fun example2() {
    assertEquals(-1, solution1("y2024_d12_example.txt"))
  }

  @Test
  fun mine2() {
    assertEquals(-1, solution1("y2024_d12_mine.txt"))
  }
}