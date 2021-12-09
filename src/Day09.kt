fun main() {

    fun part1(input: List<String>): Int {
        val map: Array<IntArray> = Array(input.size) { i ->
            input[i].map(Char::digitToInt).toIntArray()
        }

        val xMax = input.first().lastIndex
        val yMax = input.lastIndex
        fun neighbouringValues(x: Int, y: Int) = sequence {
            if (x > 0) yield(map[y][x - 1])
            if (x < xMax) yield(map[y][x + 1])
            if (y > 0) yield(map[y - 1][x])
            if (y < yMax) yield(map[y + 1][x])
        }

        var sumOfRisk = 0

        for (x in 0..xMax) {
            for (y in 0..yMax) {
                val current = map[y][x]
                if (neighbouringValues(x, y).all { it > current }) {
                    sumOfRisk += (1 + current)
                }
            }
        }

        return sumOfRisk
    }

    fun part2(input: List<String>): Int {
        val xMax = input.first().lastIndex
        val yMax = input.lastIndex

        val map = IntArray((xMax + 1) * (yMax + 1))
        fun index(x: Int, y: Int) = (y * (xMax + 1) + x)
        operator fun IntArray.get(x: Int, y: Int) = get(index(x, y))
        operator fun IntArray.set(x: Int, y: Int, value: Int) = set(index(x, y), value)

        input.forEachIndexed { i, line ->
            line.forEachIndexed { j, char ->
                map[j, i] = char.digitToInt()
            }
        }

        fun neighbouringValues(x: Int, y: Int) = sequence {
            if (x > 0) yield(map[x - 1, y])
            if (x < xMax) yield(map[x + 1, y])
            if (y > 0) yield(map[x, y - 1])
            if (y < yMax) yield(map[x, y + 1])
        }

        data class Point(val x: Int, val y: Int)

        fun neighbouringPoints(x: Int, y: Int) = sequence {
            if (x > 0) yield(Point(x - 1, y))
            if (x < xMax) yield(Point(x + 1, y))
            if (y > 0) yield(Point(x, y - 1))
            if (y < yMax) yield(Point(x, y + 1))
        }

        fun height(p: Point) = map[p.x, p.y]

        val basins = mutableSetOf<MutableSet<Point>>()

        for (x in 0..xMax) {
            for (y in 0..yMax) {
                val current = map[x, y]
                if (neighbouringValues(x, y).all { it > current }) {
                    basins += mutableSetOf(Point(x, y))
                }
            }
        }

        fun expand(basin: MutableSet<Point>, with: Point) {
            if (with in basin) return

            val current = height(with)
            if (current == 9) return

            basin += with
            neighbouringPoints(with.x, with.y).forEach {
                expand(basin, it)
            }
        }

        basins.forEach {
            val start = it.first()
            it.remove(start)
            expand(it, start)
        }

        return basins.sortedByDescending(Set<Point>::size).take(3).fold(1) { acc, basin ->
            acc * basin.size
        }
    }

    val testInput = readInput("Day09_Test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}
