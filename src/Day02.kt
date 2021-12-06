fun main() {
    fun part1(input: List<String>): Int {
        var depth = 0
        var horizontal = 0

        input.asSequence()
            .map { it.split(" ") }
            .forEach { (command, param) ->
                val value = param.toInt()
                when (command) {
                    "forward" -> horizontal += value
                    "up" -> depth -= value
                    "down" -> depth += value
                }
            }

        return depth * horizontal
    }

    fun part2(input: List<String>): Int {
        var depth = 0
        var horizontal = 0
        var aim = 0

        input.asSequence()
            .map { it.split(" ") }
            .forEach { (command, param) ->
                val value = param.toInt()
                when (command) {
                    "forward" -> {
                        horizontal += value
                        depth += aim * value
                    }
                    "up" -> aim -= value
                    "down" -> aim += value
                }
            }

        return depth * horizontal
    }

    val testInput = readInput("Day02_Test")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
