import java.util.*

fun main() {
    fun print(image: MutableMap<Pair<Int, Int>, Boolean>) {
        for (y in (image.minOf { it.key.second })..(image.maxOf { it.key.second })) {
            for (x in (image.minOf { it.key.first })..(image.maxOf { it.key.first })) {
                print(if (image[x to y] == true) "#" else ".")
            }
            println()
        }
        println()
    }

    fun process(input: List<String>, times: Int): Int {
        val algorithm = input.first()

        var defVal = false
        fun createImage() = mutableMapOf<Pair<Int, Int>, Boolean>().withDefault { defVal }

        var image = createImage()
        input.drop(2).forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                image[x to y] = char == '#'
            }
        }

        var minY = 0
        var maxY = input.lastIndex - 2
        var minX = 0
        var maxX = input[3].lastIndex

        fun enhance(image: MutableMap<Pair<Int, Int>, Boolean>): MutableMap<Pair<Int, Int>, Boolean> {
            val offsets = listOf(
                -1 to -1, 0 to -1, 1 to -1,
                -1 to 0, 0 to 0, 1 to 0,
                -1 to 1, 0 to 1, 1 to 1,
            )

            val newImage = createImage()

            minX--
            maxX++
            minY--
            maxY++

            for (x in minX..maxX) {
                for (y in minY..maxY) {
                    val area = offsets.map { (ox, oy) -> image.getValue(x + ox to y + oy) }
                        .joinToString(separator = "") { if (it) "1" else "0" }
                    newImage[x to y] = algorithm[area.toInt(radix = 2)] == '#'
                }
            }

            return newImage
        }

        repeat(times) {
            image = enhance(image)
            if (algorithm[0] == '#') {
                defVal = !defVal
            }
        }

        return image.entries.count { it.value }
    }

    fun part1(input: List<String>): Int {
        return process(input, 2)
    }

    fun part2(input: List<String>): Int {
        return process(input, 50)
    }

    val testInput = readInput("Day20_Test")
    check(part1(testInput) == 35)
    check(part2(testInput) == 3351)

    val input = readInput("Day20")
    println(part1(input))
    println(part2(input))
}
