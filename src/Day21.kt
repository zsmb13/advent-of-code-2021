fun main() {

    fun part1(input: List<String>): Int {
        var p1 = input[0].last().digitToInt() - 1
        var p2 = input[1].last().digitToInt() - 1

        var s1 = 0
        var s2 = 0

        val die = generateSequence(1) { x ->
            if (x == 100) 1 else x + 1
        }

        var rolls = 0

        die
            .chunked(3)
            .map(List<Int>::sum)
            .onEach { rolls++ }
            .forEach { value ->
                if (rolls % 2 == 1) {
                    p1 = (p1 + value) % 10
                    s1 += p1 + 1

                    if (s1 >= 1000) {
                        return rolls * 3 * s2
                    }
                } else {
                    p2 = (p2 + value) % 10
                    s2 += p2 + 1

                    if (s2 >= 1000) {
                        return rolls * 3 * s1
                    }
                }
            }

        error("Simulation failed")
    }

    fun part2(input: List<String>): Long {
        val p1 = input[0].last().digitToInt() - 1
        val p2 = input[1].last().digitToInt() - 1

        val cache = mutableMapOf<List<Int>, Pair<Long, Long>>()

        fun Pair<Long, Long>.flip() = Pair(second, first)

        fun compute(
            p1: Int,
            p2: Int,
            s1: Int,
            s2: Int,
            rollIndex: Int,
        ): Pair<Long, Long> {
            if (s2 >= 21) return Pair(0, 1)

            val key = listOf(p1, p2, s1, s2, rollIndex)
            val match = cache[key]
            if (match != null) {
                return match
            }

            return when (rollIndex) {
                0, 1 -> (1..3).map { value ->
                    compute(
                        p1 = (p1 + value) % 10,
                        p2 = p2,
                        s1 = s1,
                        s2 = s2,
                        rollIndex = (rollIndex + 1) % 3
                    )
                }
                2 -> (1..3).map { value ->
                    val newPos = (p1 + value) % 10
                    compute(
                        p1 = p2,
                        p2 = newPos,
                        s1 = s2,
                        s2 = s1 + newPos + 1,
                        rollIndex = (rollIndex + 1) % 3
                    ).flip()
                }
                else -> error("invalid")
            }.reduce { x, y ->
                Pair(x.first + y.first, x.second + y.second)
            }.also { result ->
                cache[key] = result
            }
        }

        val (s1, s2) = compute(p1 = p1, p2 = p2, s1 = 0, s2 = 0, rollIndex = 0)
        return maxOf(s1, s2)
    }

    val testInput = readInput("Day21_Test")
    check(part1(testInput) == 739785)
    check(part2(testInput) == 444356092776315L)

    val input = readInput("Day21")
    println(part1(input))
    println(part2(input))
}
