sealed class SFNumber {
    var parent: SFPair? = null
}

class SFPair(
    left: SFNumber,
    right: SFNumber,
) : SFNumber() {
    var left: SFNumber = left
        set(value) {
            field = value
            value.parent = this
        }
    var right: SFNumber = right
        set(value) {
            field = value
            value.parent = this
        }

    init {
        left.parent = this
        right.parent = this
    }
}

data class SFValue(
    var value: Int,
) : SFNumber()

fun parsePair(str: String): SFPair {
    var lefts = 0
    var rights = 0
    str.forEachIndexed { index, char ->
        when (char) {
            '[' -> lefts++
            ']' -> rights++
            ',' -> {
                if (lefts == rights) {
                    val leftStr = str.substring(0, index)
                    val rightStr = str.substring(index + 1)
                    return SFPair(left = parse(leftStr), right = parse(rightStr))
                }
            }
        }
    }
    error("Parsing failed")
}

fun SFNumber.copy(): SFNumber {
    return when (this) {
        is SFValue -> SFValue(this.value)
        is SFPair -> SFPair(left.copy(), right.copy())
    }
}

fun parseValue(str: String): SFValue = SFValue(str.toInt())

fun parse(string: String): SFNumber {
    return when (string.first()) {
        '[' -> parsePair(string.drop(1).dropLast(1))
        else -> parseValue(string)
    }
}

fun add(a: SFNumber, b: SFNumber): SFNumber {
    return reduce(SFPair(a.copy(), b.copy()))
}

fun string(sfNumber: SFNumber?): String {
    return when (sfNumber) {
        null -> "null"
        is SFValue -> sfNumber.value.toString()
        is SFPair -> "[${string(sfNumber.left)},${string(sfNumber.right)}]"
    }
}

fun reduce(sfNumber: SFNumber): SFNumber {
    fun findPairToExplode(sfNumber: SFNumber, depth: Int): SFPair? {
        return when (sfNumber) {
            is SFValue -> null
            is SFPair -> {
                if (depth >= 4 && sfNumber.left is SFValue && sfNumber.right is SFValue) sfNumber
                else {
                    findPairToExplode(sfNumber.left, depth + 1)
                        ?: findPairToExplode(sfNumber.right, depth + 1)
                }
            }
        }
    }

    fun findNodeLeftOf(sfNumber: SFNumber): SFValue? {
        val parent = sfNumber.parent ?: return null

        return if (parent.left == sfNumber) {
            findNodeLeftOf(parent)
        } else {
            var left = parent.left
            while (left is SFPair) {
                left = left.right
            }
            left as SFValue
        }
    }

    fun findNodeRightOf(sfNumber: SFNumber): SFValue? {
        val parent = sfNumber.parent ?: return null

        return if (parent.right == sfNumber) {
            findNodeRightOf(parent)
        } else {
            var right = parent.right
            while (right is SFPair) {
                right = right.left
            }
            right as SFValue
        }
    }

    fun findValueToSplit(sfNumber: SFNumber): SFValue? {
        return when (sfNumber) {
            is SFValue -> if (sfNumber.value >= 10) sfNumber else null
            is SFPair -> {
                findValueToSplit(sfNumber.left) ?: findValueToSplit(sfNumber.right)
            }
        }
    }

    fun SFNumber.replace(sfNumber: SFNumber) {
        val parent = this.parent ?: error("No parent for number to replace")
        if (parent.left == this) {
            parent.left = sfNumber
        } else {
            parent.right = sfNumber
        }
    }

    while (true) {
        val toExplode = findPairToExplode(sfNumber, depth = 0)
        if (toExplode != null) {
            val left = findNodeLeftOf(toExplode)
            if (left != null) {
                left.value += (toExplode.left as SFValue).value
            }
            val right = findNodeRightOf(toExplode)
            if (right != null) {
                right.value += (toExplode.right as SFValue).value
            }
            toExplode.replace(SFValue(0))
            continue
        }

        val toSplit = findValueToSplit(sfNumber)
        if (toSplit != null) {
            val leftValue = toSplit.value / 2
            val rightValue = toSplit.value - leftValue
            toSplit.replace(SFPair(SFValue(leftValue), SFValue(rightValue)))
            continue
        }

        break
    }

    return sfNumber
}

fun magnitude(sfNumber: SFNumber): Int {
    return when (sfNumber) {
        is SFPair -> 3 * magnitude(sfNumber.left) + 2 * magnitude(sfNumber.right)
        is SFValue -> sfNumber.value
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val res = input
            .map { parse(it) }
            .reduce { a, n -> add(a, n) }
        return magnitude(res)
    }

    fun part2(input: List<String>): Int {
        val numbers: List<SFNumber> = input.map(::parse)
        return numbers.maxOf { a ->
            numbers.maxOf { b ->
                magnitude(add(a, b))
            }
        }
    }

    val testInput = readInput("Day18_Test")
    check(part1(testInput) == 4140)
    check(part2(testInput) == 3993)

    val input = readInput("Day18")
    println(part1(input))
    println(part2(input))
}
