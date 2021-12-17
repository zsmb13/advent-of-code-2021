import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val yMin = input.first().substringAfter("y=").substringBefore("..").toInt()
        val maxSpeed = (abs(yMin) - 1)
        return (maxSpeed * (maxSpeed + 1)) / 2
    }

    fun part2(input: List<String>): Int {
        val (xMin, xMax) = input.first().substringAfter("x=").substringBefore(",").split("..").map(String::toInt)
        val (yMin, yMax) = input.first().substringAfter("y=").split("..").map(String::toInt)

        tailrec fun simulate(px: Int, py: Int, vx: Int, vy: Int): Boolean {
            if (px in xMin..xMax && py in yMin..yMax) return true
            if (px > xMax || py < yMin) return false

            return simulate(
                px = px + vx,
                py = py + vy,
                vx = if (vx > 0) vx - 1 else vx,
                vy = vy - 1,
            )
        }

        val n = maxOf(xMax, abs(yMin))
        return (1..n).sumOf { vx ->
            (-n..n).count { vy ->
                simulate(px = 0, py = 0, vx = vx, vy = vy)
            }
        }
    }

    val testInput = readInput("Day17_Test")
    check(part1(testInput) == 45)
    check(part2(testInput) == 112)

    val input = readInput("Day17")
    println(part1(input))
    println(part2(input))
}
