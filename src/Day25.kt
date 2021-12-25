fun main() {
    fun part1(input: List<String>): Int {
        val map: Array<CharArray> = input.map { line ->
            line.toCharArray()
        }.toTypedArray()

        val xSize = input.first().length
        val ySize = input.size

        fun progress(m: Array<CharArray>): Boolean {
            fun moveRight(): Boolean {
                var moved = false

                for (y in 0 until ySize) {
                    val temp = CharArray(xSize)
                    for (x in 0 until xSize) {
                        if (temp[x] != '\u0000') continue

                        val nextX = (x + 1) % xSize
                        if (m[y][x] == '>' && m[y][nextX] == '.') {
                            temp[x] = '.'
                            temp[nextX] = '>'
                            moved = true
                        } else {
                            temp[x] = m[y][x]
                        }
                    }
                    for (x in 0 until xSize) {
                        m[y][x] = temp[x]
                    }
                }
                return moved
            }

            fun moveDown(): Boolean {
                var moved = false

                for (x in 0 until xSize) {
                    val temp = CharArray(ySize)
                    for (y in 0 until ySize) {
                        if (temp[y] != '\u0000') continue

                        val nextY = (y + 1) % ySize
                        if (m[y][x] == 'v' && m[nextY][x] == '.') {
                            temp[y] = '.'
                            temp[nextY] = 'v'
                            moved = true
                        } else {
                            temp[y] = m[y][x]
                        }
                    }
                    for (y in 0 until ySize) {
                        m[y][x] = temp[y]
                    }
                }
                return moved
            }

            val movedRight = moveRight()
            val movedDown = moveDown()
            return movedRight || movedDown
        }

        return generateSequence { progress(map) }
            .takeWhile { it }
            .count() + 1
    }

    val testInput = readInput("Day25_Test")
    check(part1(testInput) == 58)

    val input = readInput("Day25")
    println(part1(input))
}
