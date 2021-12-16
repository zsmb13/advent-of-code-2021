fun main() {
    data class Dot(val x: Int, val y: Int)
    data class Fold(val axis: String, val value: Int)

    fun Fold.apply(dot: Dot): Dot {
        fun flip(value: Int, around: Int): Int {
            if (value < around) return value
            return around - (value - around)
        }
        return when (axis) {
            "x" -> Dot(flip(dot.x, value), dot.y)
            else -> Dot(dot.x, flip(dot.y, value))
        }
    }

    fun parseDots(input: List<String>): Set<Dot> {
        return buildSet {
            input.takeWhile(String::isNotEmpty).map {
                val (x, y) = it.split(",").map(String::toInt)
                add(Dot(x, y))
            }
        }
    }

    fun parseFolds(input: List<String>): List<Fold> {
        return input.takeLastWhile(String::isNotEmpty).map {
            val (axis, value) = it.substringAfter("fold along ").split("=")
            Fold(axis, value.toInt())
        }
    }

    fun print(dots: Iterable<Dot>) {
        for (y in 0..(dots.maxOf { it.y })) {
            for (x in 0..(dots.maxOf { it.x })) {
                print(if (Dot(x, y) in dots) "#" else ".")
            }
            println()
        }
    }

    fun part1(input: List<String>): Int {
        var dots = parseDots(input)
        val folds = parseFolds(input)

        folds.first().let { fold ->
            dots = dots.map(fold::apply).toSet()
        }
        return dots.size
    }

    fun part2(input: List<String>) {
        val dots = parseDots(input)
        val folds = parseFolds(input)

        folds.fold(dots) { dots, fold ->
            dots.mapTo(mutableSetOf(), fold::apply)
        }.let(::print)
    }

    val testInput1 = readInput("Day13_Test")
    check(part1(testInput1) == 17)

    val input = readInput("Day13")
    println(part1(input))
    part2(input)
}
