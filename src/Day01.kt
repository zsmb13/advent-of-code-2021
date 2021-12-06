fun main() {
    fun part1(input: List<String>): Int {
        return input
            .asSequence()
            .map(String::toInt)
            .windowed(2)
            .count { (a, b) -> a < b }
    }

    fun part2(input: List<String>): Int {
        return input
            .asSequence()
            .map(String::toInt)
            .windowed(3)
            .map(List<Int>::sum)
            .windowed(2)
            .count { (a, b) -> a < b }
    }

    val testInput = readInput("Day01_Test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
