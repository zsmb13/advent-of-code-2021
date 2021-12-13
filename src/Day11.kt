fun main() {
    data class Octopus(
        val x: Int,
        val y: Int,
        var energy: Int,
        var flashed: Boolean = false,
    )

    class SafeGrid<T>(val elements: Array<T>, val xSize: Int, val ySize: Int) : Iterable<T> {
        init {
            require(elements.size == xSize * ySize)
        }

        private fun index(x: Int, y: Int): Int = y * xSize + x

        operator fun get(x: Int, y: Int): T? {
            if (x !in 0 until xSize || y !in 0 until ySize) {
                return null
            }
            return elements[index(x, y)]
        }

        override fun iterator(): Iterator<T> = elements.iterator()

        override fun toString(): String {
            return elements.asList().chunked(xSize)
                .joinToString("\n") { it.joinToString(separator = "") }
        }
    }

    fun similateOctopuses(
        input: List<String>,
        steps: Int,
        onFlash: () -> Unit = {},
        onStepComplete: (octopuses: SafeGrid<Octopus>) -> Boolean = { false }
    ) {
        val octopuses: SafeGrid<Octopus> = SafeGrid(
            elements = input.flatMapIndexed { y, line ->
                line.mapIndexed { x, char -> Octopus(x, y, char.digitToInt()) }
            }.toTypedArray(),
            xSize = 10,
            ySize = 10,
        )

        val offsets = listOf(
            -1 to -1, -1 to 0, -1 to 1,
            0 to -1, 0 to 1,
            1 to -1, 1 to 0, 1 to 1
        )

        fun Octopus.neighbours(): Sequence<Octopus> {
            return offsets
                .asSequence()
                .mapNotNull { (xOffset, yOffset) ->
                    octopuses[this.x + xOffset, this.y + yOffset]
                }
        }

        fun Octopus.flash() {
            if (flashed) return
            flashed = true
            onFlash()

            neighbours()
                .filterNot { it.flashed }
                .forEach { it.energy++ }

            energy = 0
        }

        repeat(steps) { step ->
            octopuses.forEach {
                it.flashed = false
                it.energy++
            }

            do {
                octopuses.asSequence()
                    .filter { it.energy > 9 }
                    .forEach { it.flash() }
            } while (octopuses.any { it.energy > 9 })

            if (onStepComplete(octopuses)) return
        }
    }

    fun part1(input: List<String>): Int {
        var flashes = 0
        similateOctopuses(input, 100, onFlash = { flashes++ })
        return flashes
    }

    fun part2(input: List<String>): Int {
        var steps = 0

        similateOctopuses(
            input = input,
            steps = 1_000_000,
            onFlash = {},
            onStepComplete = { octopuses ->
                steps++
                octopuses.all { it.flashed }
            },
        )

        return steps
    }

    val testInput = readInput("Day11_Test")
    check(part1(testInput) == 1656)
    check(part2(testInput) == 195)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}
