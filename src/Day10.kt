import java.util.*

fun main() {
    val pairs = mapOf(
        ')' to '(',
        ']' to '[',
        '}' to '{',
        '>' to '<',
    )

    fun part1(input: List<String>): Int {
        val scores = mapOf(
            ')' to 3,
            ']' to 57,
            '}' to 1197,
            '>' to 25137,
        )

        return input.sumOf { line ->
            val stack = Stack<Char>()

            line.forEach { char ->
                when (char) {
                    '(', '[', '{', '<' -> stack.add(char)
                    ')', ']', '}', '>' -> if (stack.pop() != pairs.getValue(char)) {
                        return@sumOf scores.getValue(char)
                    }
                }
            }

            0
        }
    }

    fun part2(input: List<String>): Long {
        val charScores = mapOf(
            '(' to 1,
            '[' to 2,
            '{' to 3,
            '<' to 4,
        )

        return input.mapNotNull { line ->
            val stack = Stack<Char>()

            line.forEach { char ->
                when (char) {
                    '(', '[', '{', '<' -> stack.add(char)
                    ')', ']', '}', '>' -> if (stack.pop() != pairs.getValue(char)) {
                        return@mapNotNull null
                    }
                }
            }

            stack.asReversed().fold(0L) { score, char ->
                score * 5 + charScores.getValue(char)
            }
        }.sorted().run { get(size / 2) }
    }

    val testInput = readInput("Day10_Test")
    check(part1(testInput) == 26397)
    check(part2(testInput) == 288957L)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
