package app.sixarmstudios.adventofcode

import junit.framework.TestCase
import org.junit.Test

class Y2024D05 : Shark() {

  private fun isValidPlacement(
    p: Int,
    placed: Set<Int>,
    ruleSet: Map<Int, List<Int>>,
    nums: Set<Int>,
  ): Boolean {
    val applicableRule = ruleSet.getOrElse(p) { emptyList() }
    return applicableRule.all { !nums.contains(it) || placed.contains(it) }
  }

  private fun placeSaves(
    saves: MutableSet<Int>, acc: MutableSet<Int>, ruleSet: Map<Int, List<Int>>, nums: Set<Int>,
    result: MutableList<Int>) {
    saves.toList().forEach { s ->
      if (isValidPlacement(s, acc, ruleSet, nums)) {
//          println("   >>>> injected saved $s")
        acc.add(s)
        result.add(s)
        saves.remove(s)
      }
    }
  }

  private fun correctOrder(update: List<Int>, ruleSet: Map<Int, List<Int>>): List<Int> {
    val result = mutableListOf<Int>()
    val saves = mutableSetOf<Int>()
    val nums = update.toSet()
    val acc = update.fold(mutableSetOf<Int>()) { acc, p ->
      val isValid = isValidPlacement(p, acc, ruleSet, nums)
      if (isValid) {
//        println("   >>>> placed $p")
        acc.add(p)
        result.add(p)
      }
      placeSaves(saves, acc, ruleSet, nums, result)
      if (!isValid) {
//        println("   >>>> saved $p")
        saves.add(p)
      }
      return@fold acc
    }
    var i = 0
    val report = saves.size
    while(saves.isNotEmpty() && ++i < 100) {
      placeSaves(saves, acc, ruleSet, nums, result)
    }
    if (report > 0) {
      println(" ~~~ went from $report -> ${saves.size} in $i \t\t\t\t $update -> $result")
    }
    if (saves.isNotEmpty()) {
      println("!! left over entries [$saves] from $update --> $result")
    }
    return result
  }

  private fun solution2(fileName: String): Int {
    val ruleSet = buildRuleSet(fileName)
    val updateList = buildUpdateList(fileName)
    val ruleMap = buildRuleMap(ruleSet)
    return updateList
      .filter { update -> !isValidUpdate(update, ruleMap) }
      .map { correctOrder(it, ruleMap) }
      .map { update -> findMiddle(update) }
      .sum()
  }

  private fun solution1(fileName: String): Int {
    val ruleSet = buildRuleSet(fileName)
    val updateList = buildUpdateList(fileName)
    val ruleMap = buildRuleMap(ruleSet)
    return updateList
      .filter { update ->
        isValidUpdate(update, ruleMap)
      }
      .map { update -> findMiddle(update) }
      .sum()
  }

  private fun findMiddle(update: List<Int>): Int {
    return update[update.size / 2]
  }

  private fun buildRuleMap(ruleSet: List<Pair<Int, Int>>): Map<Int, List<Int>> {
    val result = mutableMapOf<Int, List<Int>>()
    ruleSet.forEach { r ->
      val entry = result.getOrElse(r.second) { emptyList() }
      result.put(r.second, entry + listOf(r.first))
    }
    println("-- provided rule map : $result")
    return result
  }

  private fun isValidUpdate(update: List<Int>, ruleMap: Map<Int, List<Int>>): Boolean {
//    println("looking at $update")
    val nums = update.toSet()
    update.fold(mutableSetOf<Int>()) { acc, p ->
      val applicableRule = ruleMap.getOrElse(p) { emptyList() }
      if (!applicableRule.all { !nums.contains(it) || acc.contains(it) }) {
//        println("  >> bombed because $p needed $applicableRule and we only had $acc")
        return false
      }
      acc.add(p)
      return@fold acc
    }
//    println(" > passed")
    return true
  }

  /**
   * @param s - in the form "1,2,12,69,6"
   */
  private fun stringToUpdate(s: String): List<Int> {
    return s.split(",").filter { it.isNotBlank() }.map { it.toInt() }
  }

  private fun buildUpdateList(fileName: String): List<List<Int>> {
    return processFileData(loadFile(fileName))
      .filter { it.contains(",") }
      .map { stringToUpdate(it) }
  }

  /**
   * @param s -  in the form "69|12"
   */
  private fun stringToRule(s: String): Pair<Int, Int> {
    return s.split("|").filter { it.isNotBlank() }.map { it.toInt() }.let { Pair(it[0], it[1]) }
  }

  private fun buildRuleSet(fileName: String): List<Pair<Int, Int>> {
    return processFileData(loadFile(fileName))
      .filter { it.contains("|") }
      .map { stringToRule(it) }
  }

  @Test
  fun example1() {
    TestCase.assertEquals(143, solution1("y2024_d05_example.txt"))
  }

  @Test
  fun mine1() {
    TestCase.assertEquals(1, solution1("y2024_d05_mine.txt"))
  }

  @Test
  fun example2() {
    TestCase.assertEquals(123, solution2("y2024_d05_example.txt"))
  }

  @Test
  fun mine2() {
    TestCase.assertEquals(6319, solution2("y2024_d05_mine.txt"))
    // 6501 - too high
  }
}