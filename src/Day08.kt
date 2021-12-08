fun main() {

    fun part1(input: List<String>): Int {
        val uniqueLengths = setOf(2, 3, 4, 7)
        return input.sumOf { line ->
            val (patterns, outputs) = line.split(" | ")
            outputs.split(" ").count { it.length in uniqueLengths }
        }
    }

    fun part2(input: List<String>): Int {
        val digitCodes: Map<Set<Char>, Int> = mapOf(
            "abcefg" to 0,
            "cf" to 1,
            "acdeg" to 2,
            "acdfg" to 3,
            "bcdf" to 4,
            "abdfg" to 5,
            "abdefg" to 6,
            "acf" to 7,
            "abcdefg" to 8,
            "abcdfg" to 9,
        ).mapKeys { it.key.toSet() }

        fun solveOutput(input: String): Int {
            val (p, o) = input.split(" | ")
            val patterns = p.split(" ")
            val outputs = o.split(" ")

            // Mapping from randomized characters back to originals
            val solutions = mutableMapOf<Char, Char>()

            val charCounts: List<Pair<Char, Int>> =
                p.replace(" ", "").groupingBy { it }.eachCount().toList()

            // Can select the patterns for 1, 4, 7, or 8, as they use a unique
            // number of segments (2, 4, 3, and 7 segments, respectively)
            fun getPatternByUniqueLength(targetLength: Int): String {
                val matches = patterns.filter { it.length == targetLength }
                check(matches.size == 1)
                return matches.first()
            }

            // Subtracts two patterns from each other
            operator fun String.minus(other: String): Char {
                val result = toSet().minus(other.toSet())
                check(result.size == 1)
                return result.first()
            }

            // These segments are used a unique number of times within 0-9
            val e = charCounts.first { (_, count) -> count == 4 }.first
            val b = charCounts.first { (_, count) -> count == 6 }.first
            val f = charCounts.first { (_, count) -> count == 9 }.first
            solutions[e] = 'e'
            solutions[b] = 'b'
            solutions[f] = 'f'

            // 7 and 1 share two segments, the remaining digit in 7 is known to be 'a'
            val seven = getPatternByUniqueLength(3)
            val one = getPatternByUniqueLength(2)
            val a = seven - one
            solutions[a] = 'a'

            // The 'a' and 'c' segments are the only ones that are used 8 times within 0-9
            val c = charCounts.first { (char, count) -> count == 8 && char != a }.first
            solutions[c] = 'c'

            // 4 uses four segments, and we already know three of them, the last must be 'd'
            val four = getPatternByUniqueLength(4)
            val d = four.first { it !in setOf(b, c, f) }
            solutions[d] = 'd'

            // The remaining segment must be 'g' at this point
            val g = ('a'..'g').first { it !in solutions.keys }
            solutions[g] = 'g'

            return outputs.map { pattern ->
                val decodedSegments = pattern.map(solutions::getValue).toSet()
                digitCodes.getValue(decodedSegments)
            }.joinToString(separator = "").toInt()
        }

        return input.sumOf(::solveOutput)
    }

    val testInput = readInput("Day08_Test")
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
