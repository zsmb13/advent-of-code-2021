fun main() {

    data class Area(
        val x1: Long, val x2: Long,
        val y1: Long, val y2: Long,
        val z1: Long, val z2: Long,
    ) {
        val x = x1..x2
        val y = y1..y2
        val z = z1..z2
    }

    fun Area.volume() = (x2 - x1 + 1) * (y2 - y1 + 1) * (z2 - z1 + 1)

    operator fun LongRange.contains(other: LongRange): Boolean =
        other.first in this && other.last in this

    operator fun Area.contains(other: Area): Boolean =
        other.x in x && other.y in y && other.z in z

    fun LongRange.intersects(other: LongRange): Boolean =
        other.first in this || other.last in this || other in this || this in other

    fun Area.intersects(other: Area): Boolean =
        x.intersects(other.x) && y.intersects(other.y) && z.intersects(other.z)

    fun LongRange.splitBy(other: LongRange): List<LongRange> {
        if (first < other.first && last in other) {
            return listOf(
                first until other.first,
                other.first..last,
            )
        }
        if (last > other.last && first in other) {
            return listOf(
                first..other.last,
                (other.last + 1)..last,
            )
        }
        if (other in this) {
            return listOfNotNull(
                (first until other.first).takeUnless(LongRange::isEmpty),
                other,
                ((other.last + 1)..last).takeUnless(LongRange::isEmpty),
            )
        }
        error("$this and $other don't intersect")
    }

    fun Area.subtract(other: Area): List<Area> {
        if (this in other) return emptyList()
        if (!intersects(other)) return listOf(this)

        return when {
            x.intersects(other.x) && x !in other.x -> {
                x.splitBy(other.x)
                    .map { range -> copy(x1 = range.first, x2 = range.last) }
            }
            y.intersects(other.y) && y !in other.y -> {
                y.splitBy(other.y)
                    .map { range -> copy(y1 = range.first, y2 = range.last) }
            }
            z.intersects(other.z) && z !in other.z -> {
                z.splitBy(other.z)
                    .map { range -> copy(z1 = range.first, z2 = range.last) }
            }
            else -> error("Invalid inputs")
        }.flatMap { it.subtract(other) }
    }

    fun parse(input: List<String>): List<Pair<Area, Boolean>> {
        return input.map { line ->
            val (onOrOff, coords) = line.split(" ")
            val (x, y, z) = coords.split(",")

            fun String.values() =
                this.substringAfter('=').split("..").map(String::toLong)
            val (x1, x2) = x.values()
            val (y1, y2) = y.values()
            val (z1, z2) = z.values()

            Pair(
                Area(x1, x2, y1, y2, z1, z2),
                onOrOff == "on",
            )
        }
    }

    fun reboot(input: List<String>): Long {
        return parse(input)
            .fold(emptyList<Area>()) { active, (area, on) ->
                val existing = active.flatMap { it.subtract(area) }
                if (on) existing + area else existing
            }
            .sumOf(Area::volume)
    }

    val testInput1 = readInput("Day22_Test1")
    check(reboot(testInput1) == 39L)
    val testInput2 = readInput("Day22_Test2")
    check(reboot(testInput2) == 590784L)

    val input1 = readInput("Day22_1")
    println(reboot(input1))

    val input2 = readInput("Day22_2")
    println(reboot(input2))
}
