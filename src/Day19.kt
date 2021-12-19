import kotlin.math.abs

data class Point(var a: Int, var b: Int, var c: Int) {
    operator fun get(index: Int): Int {
        return when (index) {
            0 -> a
            1 -> b
            2 -> c
            else -> error("invalid index")
        }
    }
}

data class Scanner(
    val position: Point = Point(0, 0, 0),
    val beacons: List<Point>,
)

fun main() {
    fun parseScanners(input: List<String>): List<Scanner> {
        return buildList {
            var beacons = mutableListOf<Point>()
            input.forEach { line ->
                if (line.startsWith("--- scanner")) {
                    // Do nothing
                } else if (line.isBlank()) {
                    add(Scanner(beacons = beacons))
                    beacons = mutableListOf()
                } else {
                    val (a, b, c) = line.split(",").map(String::toInt)
                    beacons += Point(a, b, c)
                }
            }
            add(Scanner(beacons = beacons))
        }
    }

    fun computeSolution(scanners: MutableList<Scanner>): Pair<MutableList<Scanner>, Set<Point>> {
        fun pow2(x: Int) = x * x

        val diffSets = scanners.map { s ->
            s.beacons.flatMap { b1 ->
                s.beacons.map { b2 -> pow2(b1.a - b2.a) + pow2(b1.b - b2.b) + pow2(b1.c - b2.c) }
            }.toSet()
        }

        val matches = mutableSetOf<Pair<Int, Int>>()
        diffSets.forEachIndexed { i, si ->
            diffSets.forEachIndexed { j, sj ->
                if (i < j) {
                    val size = si.intersect(sj).size
                    if (size >= 66) {
                        matches.add(i to j)
                    }
                }
            }
        }

        val rotations = listOf("012", "021", "102", "120", "201", "210")
        val flips = listOf("000", "001", "010", "100", "011", "101", "110", "111")

        fun Scanner.rotations(): Sequence<Scanner> {
            fun Point.rotate(rotation: String, flip: String): Point {
                fun map(rotation: Char, flip: Char): Int {
                    val temp = get(rotation.digitToInt())
                    return if (flip == '1') -temp else temp
                }
                return Point(
                    a = map(rotation[0], flip[0]),
                    b = map(rotation[1], flip[1]),
                    c = map(rotation[2], flip[2]),
                )
            }

            return sequence {
                rotations.forEach { rotation ->
                    flips.forEach { flip ->
                        yield(
                            Scanner(
                                position = position.rotate(rotation, flip),
                                beacons = beacons.map { it.rotate(rotation, flip) },
                            )
                        )
                    }
                }
            }
        }

        fun rotate(target: Scanner, subject: Scanner): Scanner {
            fun Point.offset(amount: Point) {
                a += amount.a
                b += amount.b
                c += amount.c
            }

            subject.rotations().forEach { rotation ->
                val counts = rotation.beacons.flatMap { rb ->
                    target.beacons.map { tb ->
                        Point((tb.a - rb.a), (tb.b - rb.b), (tb.c - rb.c))
                    }
                }.groupingBy { it }.eachCount()

                val offset = counts.entries.find { it.value >= 12 }
                if (offset != null) {
                    val offsetAmount = offset.key
                    rotation.beacons.forEach { it.offset(offsetAmount) }
                    rotation.position.offset(offsetAmount)
                    return rotation
                }
            }
            error("no matching rotations")
        }

        val solvedScanners = BooleanArray(size = scanners.size)
        solvedScanners[0] = true

        while (solvedScanners.any { !it }) {
            for ((si1, si2) in matches) {
                if (solvedScanners[si1] && solvedScanners[si2]) continue

                if (solvedScanners[si1]) {
                    scanners[si2] = rotate(target = scanners[si1], subject = scanners[si2])
                    solvedScanners[si2] = true
                } else if (solvedScanners[si2]) {
                    scanners[si1] = rotate(target = scanners[si2], subject = scanners[si1])
                    solvedScanners[si1] = true
                }
            }
        }

        return Pair(scanners, scanners.flatMap { it.beacons }.toSet())
    }

    fun part1(input: List<String>): Int {
        val sc = parseScanners(input).toMutableList()
        val (_, beacons) = computeSolution(sc)
        return beacons.size
    }


    fun part2(input: List<String>): Int {
        val sc = parseScanners(input).toMutableList()
        val (scanners, _) = computeSolution(sc)

        fun manhattan(p1: Point, p2: Point) = abs(p1.a - p2.a) + abs(p1.b - p2.b) + abs(p1.c - p2.c)

        return scanners.maxOf { s1 ->
            scanners.maxOf { s2 -> manhattan(s1.position, s2.position) }
        }
    }

    val testInput = readInput("Day19_Test")
    check(part1(testInput) == 79)
    check(part2(testInput) == 3621)

    val input = readInput("Day19")
    println(part1(input))
    println(part2(input))
}
