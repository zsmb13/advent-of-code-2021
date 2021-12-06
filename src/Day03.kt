fun main() {
    fun part1(input: List<String>): Int {
        val numberOfBits = input.first().length
        val bits = IntArray(size = numberOfBits)
        val mask = IntArray(size = numberOfBits) { 1 }

        fun IntArray.toInt(): Int = this
            .map { bit -> if (bit < 0) 0 else 1 }
            .joinToString(separator = "")
            .toInt(radix = 2)

        input.forEach { number ->
            number.forEachIndexed { index, bit ->
                when (bit) {
                    '0' -> bits[index]--
                    '1' -> bits[index]++
                }
            }
        }

        val gamma = bits.toInt()
        val epsilon = bits.toInt() xor mask.toInt()

        return gamma * epsilon
    }

    fun part2(input: List<String>): Int {
        val oxCandidates = input.toMutableList()
        val co2Candidates = input.toMutableList()

        var index = 0
        do {
            val mostCommonBit = oxCandidates
                .count { line -> line[index] == '1' }
                .let { count ->
                    if (count * 2 >= oxCandidates.size) '1' else '0'
                }
            oxCandidates.retainAll { it[index] == mostCommonBit }
            index++
        } while (oxCandidates.size > 1)

        index = 0
        do {
            val leastCommonBit = co2Candidates
                .count { line -> line[index] == '1' }
                .let { count -> if (count * 2 >= co2Candidates.size) '0' else '1' }
            co2Candidates.retainAll { it[index] == leastCommonBit }
            index++
        } while (co2Candidates.size > 1)

        return oxCandidates.first().toInt(radix = 2) * co2Candidates.first().toInt(radix = 2)
    }

    val testInput = readInput("Day03_Test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
