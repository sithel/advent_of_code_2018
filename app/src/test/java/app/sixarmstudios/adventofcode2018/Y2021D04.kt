package app.sixarmstudios.adventofcode2018

import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.io.BufferedReader
import java.io.InputStreamReader

class Y2021D04 {

    @Test
    fun part1_sample() {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream("y2021_d04_sample.txt")
        val boards = BufferedReader(InputStreamReader(inputStream)).readText()
                .split("\n\n")
                .map {
                    it.split("\n")
                            .filter { it.isNotBlank() }
                            .map {
                                it.split(" ")
                                        .map { it.trim() }
                                        .filter { it.isNotBlank() }
                            }
                }
                .map { Board(it) }
        val moves = "7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1"
                .split(",")
        boards.forEach { println(it) }
        println(moves)
        println("~~~~")
        (0 until boards.size).forEach { i -> println("Board : $i ") }
        val stop = moves.firstOrNull() { move -> boards.any { board -> board.move(move) } }
        println("We stopped @ $stop")
        val winner = boards.firstOrNull { it.isDone() }
        val score = winner?.sumOfUnmarked()?.times(stop?.toInt() ?: 0)
        println("Sum : ${winner?.sumOfUnmarked()} *  ${stop} = $score ")
        println(boards.map { it.isDone() })
        assertThat("is good", score == 4512)
    }

    @Test
    fun part1_mine() {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream("y2021_d04_mine.txt")
        val boards = BufferedReader(InputStreamReader(inputStream)).readText()
                .split("\n\n")
                .map {
                    it.split("\n")
                            .filter { it.isNotBlank() }
                            .map {
                                it.split(" ")
                                        .map { it.trim() }
                                        .filter { it.isNotBlank() }
                            }
                }
                .map { Board(it) }
        val moves = "68,30,65,69,5,78,41,73,55,0,76,98,79,42,37,21,9,34,56,33,64,54,24,43,15,58,61,38,12,20,4,26,87,95,94,89,83,74,97,77,67,40,63,88,19,31,81,80,60,14,18,47,93,57,17,90,84,85,48,6,91,7,86,13,51,53,8,16,23,66,36,39,32,82,72,11,52,28,62,70,59,50,1,46,96,71,35,10,25,22,27,99,29,45,44,3,75,92,49,2"
                .split(",")
        boards.forEach { println(it) }
        println(moves)
        println("~~~~")
        (0 until boards.size).forEach { i -> println("Board : $i ") }
        val stop = moves.firstOrNull() { move -> boards.any { board -> board.move(move) } }
        println("We stopped @ $stop")
        val winner = boards.firstOrNull { it.isDone() }
        val score = winner?.sumOfUnmarked()?.times(stop?.toInt() ?: 0)
        println("Sum : ${winner?.sumOfUnmarked()} *  ${stop} = $score ")
        println(boards.map { it.isDone() })
        assertThat("is good", score == 51776)
    }


    @Test
    fun part2_sample() {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream("y2021_d04_sample.txt")
        val boards = BufferedReader(InputStreamReader(inputStream)).readText()
                .split("\n\n")
                .map {
                    it.split("\n")
                            .filter { it.isNotBlank() }
                            .map {
                                it.split(" ")
                                        .map { it.trim() }
                                        .filter { it.isNotBlank() }
                            }
                }
                .map { Board(it) }
        val moves = "7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1"
                .split(",")
        boards.forEach { println(it) }
        println(moves)
        println("~~~~")
        (0 until boards.size).forEach { i -> println("Board : $i ") }
        val wins = mutableListOf<Board>()
        val stop = moves.firstOrNull() { move ->
            boards.forEach { board ->
                val win = board.move(move)
                println(" move [$move] -> $win & ${wins.contains(board)}")
                if (win && !wins.contains(board)) {
                    wins.add(board)
                }
            }
            boards.all { it.isDone() }
        }
        println("We stopped @ $stop")
        val winner = wins.last()
        val score = winner?.sumOfUnmarked()?.times(stop?.toInt() ?: 0)
        println("Sum : ${winner?.sumOfUnmarked()} *  ${stop} = $score ")
        println(boards.map { it.isDone() })
        assertThat("is good", score == 1924)
    }


    @Test
    fun part2_mine() {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream("y2021_d04_mine.txt")
        val boards = BufferedReader(InputStreamReader(inputStream)).readText()
                .split("\n\n")
                .map {
                    it.split("\n")
                            .filter { it.isNotBlank() }
                            .map {
                                it.split(" ")
                                        .map { it.trim() }
                                        .filter { it.isNotBlank() }
                            }
                }
                .map { Board(it) }
        val moves = "68,30,65,69,5,78,41,73,55,0,76,98,79,42,37,21,9,34,56,33,64,54,24,43,15,58,61,38,12,20,4,26,87,95,94,89,83,74,97,77,67,40,63,88,19,31,81,80,60,14,18,47,93,57,17,90,84,85,48,6,91,7,86,13,51,53,8,16,23,66,36,39,32,82,72,11,52,28,62,70,59,50,1,46,96,71,35,10,25,22,27,99,29,45,44,3,75,92,49,2"
                .split(",")
        boards.forEach { println(it) }
        println(moves)
        println("~~~~")
        (0 until boards.size).forEach { i -> println("Board : $i ") }
        val wins = mutableListOf<Board>()
        val stop = moves.firstOrNull() { move ->
            boards.forEach { board ->
                val win = board.move(move)
                println(" move [$move] -> $win & ${wins.contains(board)}")
                if (win && !wins.contains(board)) {
                    wins.add(board)
                }
            }
            boards.all { it.isDone() }
        }
        println("We stopped @ $stop")
        val winner = wins.last()
        val score = winner?.sumOfUnmarked()?.times(stop?.toInt() ?: 0)
        println("Sum : ${winner?.sumOfUnmarked()} *  ${stop} = $score ")
        println(boards.map { it.isDone() })
        assertThat("is good", score == 16830)
    }

    data class Board(val board: List<List<String>>) {
        val moves = board.map { it.toMutableList() }
        fun move(move: String): Boolean {
            moves.forEach { row ->
                row.forEachIndexed { i, cell -> if (cell == move) row[i] = "" }
            }
//            println("  BOARD [${isDone()}] : $moves")
            return isDone()
        }

        fun isDone(): Boolean {
            val rowWin = moves.any() { row ->
                row.all { it.isBlank() }
            }
            val colWin = (0 until moves[0].size).any { column ->
                (moves.indices).all { row -> moves[row][column].isBlank() }
            }
            return rowWin || colWin
        }

        fun sumOfUnmarked(): Int {
            return moves.sumBy { row ->
                row.sumBy { cell -> if (cell.isEmpty()) 0 else cell.toInt() }
            }
        }
    }
}