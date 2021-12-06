fun main() {

    // Brute force
    fun part1(input: List<String>): Int {
        val fish = input.first().split(",").map { it.toInt() }.toMutableList()
        repeat(80) {
            fish.indices.forEach { i ->
                if (--fish[i] == -1) {
                    fish[i] = 6
                    fish.add(8)
                }
            }
        }
        return fish.count()
    }

    // Recursive, brute force, with a LUT to make it performant
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

    // Bucketing all fish of the same timer value
    fun part2Variant(input: List<String>): Long {
        val buckets = LongArray(size = 9)
        input.first().split(",")
            .map(String::toInt)
            .forEach { buckets[it]++ }

        repeat(256) {
            val spawnerCount = buckets[0]
            for (i in 1..8) {
                buckets[i - 1] = buckets[i]
            }
            buckets[8] = spawnerCount
            buckets[6] += spawnerCount
        }

        return buckets.sumOf { it }
    }

    val testInput = readInput("Day06_Test")
    check(part1(testInput) == 5934)
    check(part2(testInput) == 26984457539L)
    check(part2Variant(testInput) == 26984457539L)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
    println(part2Variant(input))
}
