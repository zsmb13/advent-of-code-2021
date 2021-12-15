fun main() {
    fun findLowestCostPath(size: Int, riskMap: IntArray): Int {
        val maxIndex = size - 1

        operator fun IntArray.get(x: Int, y: Int): Int = get(y * size + x)
        operator fun IntArray.set(x: Int, y: Int, value: Int) = set(y * size + x, value)

        val lowestCosts = IntArray(riskMap.size) { Int.MAX_VALUE }
        lowestCosts[0, 0] = 0

        val done: MutableSet<Pair<Int, Int>> = mutableSetOf()
        val targets: MutableSet<Pair<Int, Int>> = mutableSetOf(Pair(0, 0))

        while (targets.isNotEmpty()) {
            val target = targets.minByOrNull { (x, y) -> lowestCosts[x, y] }!!
            targets.remove(target)
            done.add(target)

            val (x, y) = target
            sequenceOf(x - 1 to y, x + 1 to y, x to y - 1, x to y + 1)
                .filter { (x, y) -> x in 0..maxIndex && y in 0..maxIndex }
                .filter { it !in done }
                .onEach { neighbour -> targets.add(neighbour) }
                .forEach { (nx, ny) ->
                    val distFromTarget = lowestCosts[x, y] + riskMap[nx, ny]
                    if (distFromTarget < lowestCosts[nx, ny]) {
                        lowestCosts[nx, ny] = distFromTarget
                    }
                }
        }

        return lowestCosts[maxIndex, maxIndex]
    }

    fun part1(input: List<String>): Int {
        val size = input.size
        val riskMap = input.flatMap { line -> line.map(Char::digitToInt) }.toIntArray()

        return findLowestCostPath(size, riskMap)
    }

    fun part2(input: List<String>): Int {
        val size = input.size * 5
        val riskMap = (0..4).flatMap { yCopy ->
            input.flatMap { line ->
                (0..4).flatMap { xCopy ->
                    line.map { digit ->
                        val value = digit.digitToInt() + yCopy + xCopy
                        if (value < 10) value else value - 9
                    }
                }
            }
        }.toIntArray()

        return findLowestCostPath(size, riskMap)
    }

    val testInput = readInput("Day15_Test")
    check(part1(testInput) == 40)
    check(part2(testInput) == 315)

    val input = readInput("Day15")
    println(part1(input))
    println(part2(input))
}
