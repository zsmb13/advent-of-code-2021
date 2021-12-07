import kotlin.math.abs
import kotlin.math.roundToInt

fun main() {

    fun part1(input: List<String>): Int {
        val positions = input.first().split(",").map(String::toInt).sorted()
        val mid = positions.size / 2
        val target = positions[mid]
        return positions.sumOf { position -> abs(target - position) }
    }

    fun part2(input: List<String>): Int {
        val positions = input.first().split(",").map(String::toInt)
        val average = positions.average()
        val candidates = listOf(
            average.toInt(), // losing the fractional part
            average.roundToInt(), // proper rounding
        )

        fun costTo(target: Int): Int = positions.sumOf { position ->
            fun sumTo(n: Int) = n * (n + 1) / 2

            val distance = abs(target - position)
            sumTo(distance)
        }

        return if (candidates[0] == candidates[1]) { // serious optimization move
            costTo(candidates[0])
        } else {
            candidates.minOf { target -> costTo(target) }
        }
    }

    val testInput = readInput("Day07_Test")
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
