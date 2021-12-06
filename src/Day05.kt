import kotlin.math.sign

fun main() {
    data class Point(val x: Int, val y: Int)

    data class Line(val p1: Point, val p2: Point) : Iterable<Point> {
        override fun iterator(): Iterator<Point> {
            return object : Iterator<Point> {
                val dx = (p2.x - p1.x).sign
                val dy = (p2.y - p1.y).sign

                var current = p1
                val target = Point(p2.x + dx, p2.y + dy)

                private fun step() {
                    current = Point(current.x + dx, current.y + dy)
                }

                override fun hasNext(): Boolean {
                    return current != target
                }

                override fun next(): Point {
                    return current.also { step() }
                }
            }
        }
    }

    fun getLines(
        input: List<String>,
        predicate: (Line) -> Boolean = { true },
    ): List<Line> {
        return input
            .map { line ->
                val (x1, y1, x2, y2) = line.split(" -> ").flatMap { it.split(",") }.map(String::toInt)
                Line(Point(x1, y1), Point(x2, y2))
            }
            .filter(predicate)
    }

    fun countOverlaps(lines: List<Line>): Int {
        return lines.asSequence()
            .flatMap(Line::asIterable)
            .groupingBy { point -> point }
            .eachCount()
            .filter { (_, count) -> count >= 2 }
            .count()
    }

    fun part1(input: List<String>): Int {
        val lines = getLines(input, predicate = { (p1, p2) -> p1.x == p2.x || p1.y == p2.y })
        return countOverlaps(lines)
    }

    fun part2(input: List<String>): Int {
        return countOverlaps(getLines(input))
    }

    val testInput = readInput("Day05_Test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
