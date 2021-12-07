fun main() {
    fun part1(input: List<String>): Int {
        val numberOfBits = input.first().length
        val bits = IntArray(size = numberOfBits)
        val mask = IntArray(size = numberOfBits) { 1 }

        fun IntArray.toInt(): Int = buildString {
            this@toInt.forEach { bit ->
                append(if (bit >= 0) '1' else '0')
            }
        }.toInt(radix = 2)

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

    fun runRetainCycles(oxCandidates: MutableList<String>, bitToRetain: (oneRatio: Double) -> Char) {
        var index = 0
        do {
            val mostCommonBit = oxCandidates
                .count { line -> line[index] == '1' }
                .let { count -> bitToRetain(count / oxCandidates.size.toDouble()) }
            oxCandidates.retainAll { it[index] == mostCommonBit }
            index++
        } while (oxCandidates.size > 1)
    }

    fun part2(input: List<String>): Int {
        val oxCandidates = input.toMutableList()
        val co2Candidates = input.toMutableList()

        runRetainCycles(oxCandidates) { oneRatio -> if (oneRatio >= 0.5) '1' else '0' }
        runRetainCycles(co2Candidates) { oneRatio -> if (oneRatio >= 0.5) '0' else '1' }

        return oxCandidates.first().toInt(radix = 2) * co2Candidates.first().toInt(radix = 2)
    }

    val testInput = readInput("Day03_Test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
