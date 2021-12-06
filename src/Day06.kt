fun main() {

    fun part1(input: List<String>): Int {
        val fish = input.first().split(",").map { it.toInt() }.toMutableList()
        (1..80).forEach {
            fish.indices.forEach { i ->
                if (--fish[i] == -1) {
                    fish[i] = 6
                    fish.add(8)
                }
            }
        }
        return fish.count()
    }

    val cache = mutableMapOf<Pair<Int, Int>, Long>()

    fun calculatePopulation(fish: Int, remainingDays: Int): Long {
        val cached = cache[fish to remainingDays]
        if (cached != null) {
            return cached
        }

        var remaining = remainingDays - fish
        var total = 1L
        while (remaining > 0) {
            total += calculatePopulation(8, remaining - 1)
            remaining -= 7
        }

        cache[fish to remainingDays] = total

        return total
    }

    fun part2(input: List<String>): Long {
        val fish = input.first().split(",").map { it.toInt() }.toMutableList()
        return fish.sumOf { calculatePopulation(it, 256) }
    }

    val testInput = readInput("Day06_Test")
    check(part1(testInput) == 5934)
    check(part2(testInput) == 26984457539L)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
