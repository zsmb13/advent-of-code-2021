import java.io.File

fun main() {
    fun part1(name: String): Int {
        val (boards, inputs) = readBingoInput(name)
        inputs.forEach { number ->
            boards.forEach { board ->
                board.mark(number)
                if (board.isComplete) {
                    return board.sumOfUnmarked() * number
                }
            }
        }
        error("No result")
    }

    fun part2(name: String): Int {
        val (boards, inputs) = readBingoInput(name)
        inputs.forEach { number ->
            boards.forEach { board ->
                if (!board.isComplete) {
                    board.mark(number)
                    if (boards.all(BingoBoard::isComplete)) {
                        return board.sumOfUnmarked() * number
                    }
                }
            }
        }
        error("No result")
    }

    check(part1("Day04_Test.txt") == 4512)
    check(part2("Day04_Test.txt") == 1924)

    println(part1("Day04.txt"))
    println(part2("Day04.txt"))
}

class BingoBoard(private val numbers: IntArray) {
    var isComplete = false
        private set

    val scoringIndexSets: List<List<Int>> = // rows + columns
        (0..24).chunked(5) + (0..4).map { m -> (0..24).filter { it % 5 == m } }

    fun mark(number: Int) {
        for (i in numbers.indices) {
            if (numbers[i] == number) {
                numbers[i] = -1
                break
            }
        }
        updateCompletionState()
    }

    private fun updateCompletionState() {
        scoringIndexSets.forEach { indices ->
            if (indices.all { i -> numbers[i] == -1 }) {
                isComplete = true
                return
            }
        }
    }

    fun sumOfUnmarked() = numbers.filter { it != -1 }.sum()
}

fun readBingoInput(name: String): Pair<List<BingoBoard>, IntArray> {
    return File("src", name).useLines { lines ->
        val iterator = lines.iterator()

        val inputs = iterator.next().split(",").map(String::toInt).toIntArray()
        val boards = mutableListOf<BingoBoard>()

        while (iterator.hasNext()) {
            iterator.next() // drop empty line
            val numbers: IntArray = List(5) { iterator.next() }
                .flatMap { it.trim().split(Regex("\\s+")).map(String::toInt) }
                .toIntArray()
            boards += BingoBoard(numbers)
        }

        Pair(boards, inputs)
    }
}
