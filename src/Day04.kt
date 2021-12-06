import java.io.File

fun main() {
    fun part1(name: String): Int {
        val game = readBingoInput(name)
        val score = game.scoreFirstWinningBoard()
        return score
    }

    fun part2(name: String): Int {
        val game = readBingoInput(name)
        val score = game.scoreLastWinningBoard()
        return score
    }

    check(part1("Day04_Test.txt") == 4512)
    check(part2("Day04_Test.txt") == 1924)

    println(part1("Day04.txt"))
    println(part2("Day04.txt"))
}

fun readBingoInput(name: String): BingoGame {
    return File("src", name).useLines { lines ->
        val iterator = lines.iterator()

        val inputs = iterator.next().split(",").map(String::toInt).toIntArray()
        val boards = mutableListOf<BingoBoard>()

        while (iterator.hasNext()) {
            iterator.next() // drop empty line
            val numbers: IntArray = (1..5)
                .map { iterator.next() }
                .flatMap {
                    it.trim().split(Regex("\\s+")).map(String::toInt)
                }
                .toIntArray()
            boards += BingoBoard(numbers)
        }

        BingoGame(inputs, boards)
    }
}

class BingoBoard(val numbers: IntArray) {
    var isComplete = false
        private set

    fun mark(number: Int): Boolean {
        numbers.forEachIndexed { i, n ->
            if (n == number) {
                numbers[i] = -1
                return checkIfComplete()
            }
        }
        return false
    }

    private fun checkIfComplete(): Boolean {
        (0..24).chunked(5).forEach { rowIndices ->
            if (rowIndices.all { i -> numbers[i] == -1 }) {
                isComplete = true
                return true
            }
        }
        (0..4).forEach { m ->
            val columnIndices = (0..24).filter { it % 5 == m }
            if (columnIndices.all { i -> numbers[i] == -1 }) {
                isComplete = true
                return true
            }
        }
        return false
    }

    fun sumOfUnmarked() = numbers.filter { it != -1 }.sum()
}

class BingoGame(val inputs: IntArray, val boards: List<BingoBoard>) {
    fun scoreFirstWinningBoard(): Int {
        inputs.forEach { number ->
            boards.forEach { board ->
                if (board.mark(number)) {
                    return board.sumOfUnmarked() * number
                }
            }
        }
        error("No boards completed")
    }

    fun scoreLastWinningBoard(): Int {
        inputs.forEach { number ->
            boards.forEach { board ->
                if (!board.isComplete) {
                    board.mark(number)
                    if (boards.all { it.isComplete }) {
                        return board.sumOfUnmarked() * number
                    }
                }
            }
        }
        error("No boards completed")
    }
}
