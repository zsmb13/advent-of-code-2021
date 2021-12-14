fun main() {
    data class Cave(
        val name: String,
        val neighbours: MutableSet<String> = mutableSetOf(),
    )

    fun parseCaves(input: List<String>): MutableMap<String, Cave> {
        val caves = mutableMapOf<String, Cave>()

        input.forEach {
            val (a, b) = it.split("-")
            val caveA = caves.getOrPut(a) { Cave(a) }
            val caveB = caves.getOrPut(b) { Cave(b) }
            caveA.neighbours.add(b)
            caveB.neighbours.add(a)
        }
        return caves
    }

    fun part1(input: List<String>): Int {
        val caves = parseCaves(input)

        fun countRoutes(current: Cave, previous: List<String>): Int {
            if (current.name == "end") {
                return 1
            }
            if (current.name.first().isLowerCase() && current.name in previous) {
                return 0
            }

            val newPrevious = previous + current.name
            return current.neighbours.sumOf { name ->
                countRoutes(caves.getValue(name), newPrevious)
            }
        }

        return countRoutes(caves.getValue("start"), emptyList())
    }

    fun part2(input: List<String>): Int {
        val caves = parseCaves(input)

        fun countRoutes(
            current: Cave,
            previous: List<String>,
            hadDuplicate: Boolean,
        ): Int {
            if (current.name == "start" && previous.isNotEmpty()) {
                return 0
            }
            if (current.name == "end") {
                return 1
            }
            val hasDuplicate = current.name.first().isLowerCase() && current.name in previous
            if (hasDuplicate && hadDuplicate) {
                return 0
            }

            val newPrevious = previous + current.name
            return current.neighbours.sumOf { name ->
                countRoutes(caves.getValue(name), newPrevious, hasDuplicate || hadDuplicate)
            }
        }

        return countRoutes(caves.getValue("start"), emptyList(), false)
    }

    val testInput1 = readInput("Day12_Test1")
    val testInput2 = readInput("Day12_Test2")
    val testInput3 = readInput("Day12_Test3")
    check(part1(testInput1) == 10)
    check(part1(testInput2) == 19)
    check(part1(testInput3) == 226)

    check(part2(testInput1) == 36)
    check(part2(testInput2) == 103)
    check(part2(testInput3) == 3509)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}
