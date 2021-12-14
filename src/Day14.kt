fun main() {
    fun part1(input: List<String>): Int {
        var polymer = input.first()
        val rules = input.drop(2).let { rules ->
            buildMap {
                rules.forEach { rule ->
                    val (from, to) = rule.split(" -> ")
                    put(from, from[0] + to + from[1])
                }
            }
        }

        repeat(10) {
            polymer = buildString {
                polymer.windowed(2).forEach { pair ->
                    append(rules.getOrDefault(pair, pair).dropLast(1))
                }
                append(polymer.last())
            }
        }

        val elementCounts = polymer.groupingBy { it }.eachCount()

        return elementCounts.maxOf { it.value } - elementCounts.minOf { it.value }
    }

    fun part2(input: List<String>): Long {
        val template = input.first()
        val rules = input.drop(2).let { rules ->
            buildMap {
                rules.forEach { rule ->
                    val (from, to) = rule.split(" -> ")
                    put(from, (from[0] + to) to (to + from[1]))
                }
            }
        }

        val pairCounts = mutableMapOf<String, Long>()
        template.windowed(2).forEach { pair ->
            pairCounts[pair] = pairCounts.getOrDefault(pair, 0) + 1
        }

        repeat(40) {
            pairCounts.toMap().forEach { (pair, count) ->
                val res = rules[pair]
                if (res != null) {
                    val (a, b) = res
                    pairCounts[pair] = pairCounts.getValue(pair) - count
                    pairCounts[a] = pairCounts.getOrDefault(a, 0) + count
                    pairCounts[b] = pairCounts.getOrDefault(b, 0) + count
                }
            }
        }

        val elementCounts = mutableMapOf<Char, Long>()
        pairCounts.forEach { (pair, count) ->
            elementCounts[pair[0]] = elementCounts.getOrDefault(pair[0], 0) + count
            elementCounts[pair[1]] = elementCounts.getOrDefault(pair[1], 0) + count

        }

        return (elementCounts.maxOf { it.value } + 1) / 2 -
                (elementCounts.minOf { it.value } + 1) / 2
    }

    val testInput = readInput("Day14_Test")
    check(part1(testInput) == 1588)
    check(part2(testInput) == 2188189693529L)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}
