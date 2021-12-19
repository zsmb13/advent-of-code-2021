import kotlin.math.abs

data class Point(var a: Int, var b: Int, var c: Int) {
    operator fun get(index: Int) = when (index) {
        0 -> a; 1 -> b; 2 -> c; else -> error("Invalid index")
    }
}

fun Point.offset(other: Point) {
    a += other.a
    b += other.b
    c += other.c
}

class Scanner(
    val position: Point = Point(0, 0, 0),
    val beacons: List<Point>,
)

fun main() {
    fun parseScanners(input: List<String>): List<Scanner> {
        return buildList {
            var beacons = mutableListOf<Point>()
            input.forEach { line ->
                when {
                    line.startsWith("--- scanner") -> beacons = mutableListOf()
                    line.isBlank() -> add(Scanner(beacons = beacons))
                    else -> {
                        val (a, b, c) = line.split(",").map(String::toInt)
                        beacons += Point(a, b, c)
                    }
                }
            }
            add(Scanner(beacons = beacons))
        }
    }

    fun rotate(target: Scanner, subject: Scanner): Scanner {
        // Axis swaps (by index)
        val swaps = listOf("012", "021", "102", "120", "201", "210")
        // Axis flips (1 for true)
        val flips = listOf("000", "001", "010", "100", "011", "101", "110", "111")

        fun Scanner.rotations(): Sequence<Scanner> {
            fun Point.rotate(rotation: String, flip: String): Point {
                fun map(rotation: Char, flip: Char): Int =
                    get(rotation.digitToInt()) * if (flip == '1') -1 else 1

                return Point(
                    map(rotation[0], flip[0]),
                    map(rotation[1], flip[1]),
                    map(rotation[2], flip[2]),
                )
            }

            return sequence {
                swaps.forEach { swap ->
                    flips.forEach { flip ->
                        yield(
                            Scanner(
                                position = position.rotate(swap, flip),
                                beacons = beacons.map { it.rotate(swap, flip) },
                            )
                        )
                    }
                }
            }
        }

        subject.rotations().forEach { rotation ->
            val counts = rotation.beacons.flatMap { rb ->
                target.beacons.map { tb ->
                    Point(tb[0] - rb[0], tb[1] - rb[1], tb[2] - rb[2])
                }
            }.groupingBy { it }.eachCount()

            // Find rotation where at least 12 beacons share the same
            // offsets along all axes
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

    fun computeSolution(scanners: MutableList<Scanner>): Pair<MutableList<Scanner>, Set<Point>> {
        fun pow2(x: Int) = x * x

        // Compute all distances between pairs of beacons detected by each scanner
        val diffSets = scanners.map { s ->
            s.beacons.flatMap { b1 ->
                s.beacons.map { b2 -> pow2(b1[0] - b2[0]) + pow2(b1[1] - b2[1]) + pow2(b1[2] - b2[2]) }
            }.toSet()
        }

        // Find distance sets that share at least 66 (12 choose 2) matched values,
        // indicating 12 shared beacons between the scanners
        val matches = mutableSetOf<Pair<Int, Int>>()
        diffSets.forEachIndexed { i, si ->
            diffSets.forEachIndexed { j, sj ->
                if (i < j && si.intersect(sj).size >= 66) {
                    matches.add(i to j)
                }
            }
        }

        // Align all scanners to the coordinate system of the first one
        // using the discovered matches
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

        return Pair(scanners, scanners.flatMap(Scanner::beacons).toSet())
    }

    fun part1(input: List<String>): Int {
        val sc = parseScanners(input).toMutableList()
        val (_, beacons) = computeSolution(sc)
        return beacons.size
    }

    fun part2(input: List<String>): Int {
        val sc = parseScanners(input).toMutableList()
        val (scanners, _) = computeSolution(sc)

        fun manhattan(p1: Point, p2: Point) = abs(p1[0] - p2[0]) + abs(p1[1] - p2[1]) + abs(p1[2] - p2[2])

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
