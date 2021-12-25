fun main() {
    fun validate(num: Long, program: List<String>): Boolean {
        var i = 0
        val inputs = num.toString()

        var (w, x, y, z) = List(4) { 0 }

        fun set(char: Char, value: Int) {
            when (char) {
                'w' -> w = value
                'x' -> x = value
                'y' -> y = value
                'z' -> z = value
            }
        }

        fun get(char: Char): Int {
            return when (char) {
                'w' -> w
                'x' -> x
                'y' -> y
                'z' -> z
                else -> error("invalid")
            }
        }

        fun getVal(str: String): Int {
            return str.toIntOrNull() ?: get(str[0])
        }

        program
            .map { it.split(" ") }
            .forEach { command ->
                when (command[0]) {
                    "inp" -> {
                        set(command[1][0], inputs[i++].digitToInt())
                    }
                    "add" -> {
                        set(command[1][0], get(command[1][0]) + getVal(command[2]))
                    }
                    "mul" -> {
                        set(command[1][0], get(command[1][0]) * getVal(command[2]))
                    }
                    "div" -> {
                        set(command[1][0], get(command[1][0]) / getVal(command[2]))
                    }
                    "mod" -> {
                        set(command[1][0], get(command[1][0]) % getVal(command[2]))
                    }
                    "eql" -> {
                        set(
                            command[1][0],
                            if (get(command[1][0]) == getVal(command[2])) 1 else 0
                        )
                    }
                }
            }

        return z == 0
    }

    fun generateKtCode(input: List<String>) {
        var i = 0
        input.map { it.split(" ") }
            .forEach { command ->
                when (command[0]) {
                    "inp" -> {
                        println("${command[1]} = inputs[${i++}]")
                    }
                    "add" -> {
                        println("${command[1]} += ${command[2]}")
                    }
                    "mul" -> {
                        println("${command[1]} *= ${command[2]}")
                    }
                    "div" -> {
                        println("${command[1]} /= ${command[2]}")
                    }
                    "mod" -> {
                        println("${command[1]} %= ${command[2]}")
                    }
                    "eql" -> {
                        println("${command[1]} = if(${command[1]} == ${command[2]}) 1 else 0")
                    }
                }
            }
    }

    val testInput = readInput("Day24")
    println(validate(65984919997939L, testInput))
    println(validate(11211619541713L, testInput))
}
