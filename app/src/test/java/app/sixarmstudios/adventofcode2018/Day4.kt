package app.sixarmstudios.adventofcode2018

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

/**
 * Day 4!!
 *
 * Starting at 8:12am...
 * Example input:
 * [1518-11-06 00:57] falls asleep
 * [1518-04-19 23:59] Guard #1013 begins shift
 * [1518-07-04 00:32] wakes up
 */
class Day4 {
    data class RawEntry(val minute: Int, val guard: Int, val shiftBegin: Boolean, val startSleep: Boolean, val endSleep: Boolean)
    data class GuardSleep(val id: Int, var isAsleep: Boolean, var lastMinute: Int, var totalSleep: Int)

    val entryRegex = Regex(".*?:(\\d+)] (.*)")
    val guardRegex = Regex(".*?#(\\d+).*?")
    val sampleShifts = "[1518-11-01 00:00] Guard #10 begins shift\n" +
            "[1518-11-01 00:05] falls asleep\n" +
            "[1518-11-01 00:25] wakes up\n" +
            "[1518-11-01 00:30] falls asleep\n" +
            "[1518-11-01 00:55] wakes up\n" +
            "[1518-11-01 23:58] Guard #99 begins shift\n" +
            "[1518-11-02 00:40] falls asleep\n" +
            "[1518-11-02 00:50] wakes up\n" +
            "[1518-11-03 00:05] Guard #10 begins shift\n" +
            "[1518-11-03 00:24] falls asleep\n" +
            "[1518-11-03 00:29] wakes up\n" +
            "[1518-11-04 00:02] Guard #99 begins shift\n" +
            "[1518-11-04 00:36] falls asleep\n" +
            "[1518-11-04 00:46] wakes up\n" +
            "[1518-11-05 00:03] Guard #99 begins shift\n" +
            "[1518-11-05 00:45] falls asleep\n" +
            "[1518-11-05 00:55] wakes up"

    fun solution_part1(input: List<String>): Int {
        return 5
    }

    private fun sleepiest_guard_part1(input: List<String>): Int {
        return processData(input)
                .groupBy { it.id }
                .map { entry ->
                    Pair(entry.key, entry.value.sumBy { it.totalSleep })
                }
                .maxBy { it.second }
                ?.first!!
    }

    fun sleepiest_time_part1(input: List<String>): Int {
        val guardId = sleepiest_guard_part1(input)
        processData(input)
                .filter { it.id == guardId }
                .fold(Array(59){0}) {timesheet, entry ->
                    // punch the timesheet for every minute asleep, increment by 1
                    timesheet
                }

        return 5
    }

    private fun processData(input: List<String>): List<GuardSleep> {
        return input.sorted()
                .map { processLine(-1, it) }
                .fold(mutableListOf<GuardSleep>()) { history, entry ->
                    if (history.isEmpty() || entry.shiftBegin) {
                        history.add(GuardSleep(entry.guard, false, entry.minute, 0))
                        return@fold history
                    }
                    val last = history.last()
                    if (entry.startSleep) {
                        // make a new entry and push to history
                        last.isAsleep = true
                        last.lastMinute = entry.minute
                    } else {
                        last.isAsleep = false
                        last.totalSleep = entry.minute - last.lastMinute
                        last.lastMinute = entry.minute
                    }
                    history
                }

    }

    private fun processLine(prevGuard: Int, line: String): RawEntry {
        val groups = entryRegex.matchEntire(line)?.groups!!
        val guards = guardRegex.matchEntire(groups[2]!!.value)?.groups
        val id = if (guards != null) guards[1]!!.value.toInt() else prevGuard
        return RawEntry(
                groups[1]!!.value.toInt(),
                id,
                guards != null,
                groups[2]!!.value == "falls asleep",
                groups[2]!!.value == "wakes up"
        )

    }

    @Test
    fun test_processLine_newShift() {
        val solution = RawEntry(3, 99, true, false, false)
        assertThat(processLine(16, "[1518-11-05 00:03] Guard #99 begins shift"), `is`(solution))
    }

    @Test
    fun test_processLine_sleep() {
        val solution = RawEntry(24, 16, false, true, false)
        assertThat(processLine(16, "[1518-11-03 00:24] falls asleep"), `is`(solution))
    }

    @Test
    fun test_processLine_wake() {
        val solution = RawEntry(25, 16, false, false, true)
        assertThat(processLine(16, "[1518-11-01 00:25] wakes up"), `is`(solution))
    }

    @Test
    fun example1_part1_guard() {
        val input = sampleShifts.split("\n").filter { it.isNotBlank() }
        assertThat(sleepiest_guard_part1(input), `is`(10))
    }

    @Test
    fun example1_part1_time() {
        val input = sampleShifts.split("\n").filter { it.isNotBlank() }
        assertThat(sleepiest_time_part1(input), `is`(24))
    }

    @Test
    fun example1_part1_solution() {
        val input = sampleShifts.split("\n").filter { it.isNotBlank() }
        assertThat(solution_part1(input), `is`(240))
    }
}