@file:Suppress("MoveVariableDeclarationIntoWhen")

sealed interface Packet {
    val version: Int
    fun eval(): Long
}

data class Literal(
    override val version: Int,
    val value: Long,
) : Packet {
    override fun eval() = value
}

data class Operator(
    override val version: Int,
    val typeId: Int,
    val subPackets: List<Packet>,
) : Packet {
    override fun eval(): Long {
        return when (typeId) {
            0 -> subPackets.sumOf { it.eval() }
            1 -> subPackets.fold(1) { acc, p -> acc * p.eval() }
            2 -> subPackets.minOf { it.eval() }
            3 -> subPackets.maxOf { it.eval() }
            5 -> subPackets.let { (p1, p2) -> if (p1.eval() > p2.eval()) 1 else 0 }
            6 -> subPackets.let { (p1, p2) -> if (p1.eval() < p2.eval()) 1 else 0 }
            7 -> subPackets.let { (p1, p2) -> if (p1.eval() == p2.eval()) 1 else 0 }
            else -> error("Invalid type ID")
        }
    }
}

class Parser(input: String) {
    val string: String
    var offset = 0

    init {
        val inputChars = CharArray(input.length * 4)
        input.forEachIndexed { i, hexChar ->
            val binaries = hexChar.digitToInt(16).toString(2).padStart(4, '0')
            binaries.toCharArray(inputChars, destinationOffset = i * 4)
        }
        string = String(inputChars)
    }

    private fun read(count: Int): String {
        return string.substring(offset, offset + count).also {
            offset += count
        }
    }

    private fun parseLiteral(version: Int): Literal {
        val bits = buildString {
            do {
                val next = read(5)
                append(next.drop(1))
            } while (next[0] == '1')
        }
        return Literal(version, bits.toLong(radix = 2))
    }

    private fun parseOperator(version: Int, typeId: Int): Operator {
        val lengthTypeId = read(1)
        val subPackets = buildList {
            when (lengthTypeId) {
                "0" -> {
                    val subPacketLength = read(15).toInt(radix = 2)
                    val maxOffset = offset + subPacketLength
                    while (offset < maxOffset) {
                        add(parsePacket())
                    }
                }
                "1" -> {
                    val subPacketCount = read(11).toInt(radix = 2)
                    repeat(subPacketCount) {
                        add(parsePacket())
                    }
                }
            }
        }
        return Operator(version, typeId, subPackets)
    }

    fun parsePacket(): Packet {
        val version = read(3).toInt(radix = 2)
        val typeId = read(3).toInt(radix = 2)

        val packet = when (typeId) {
            4 -> parseLiteral(version)
            else -> parseOperator(version, typeId)
        }
        return packet
    }
}

fun main() {
    fun part1(input: String): Int {
        val packet = Parser(input).parsePacket()

        fun sumVersionNumbers(packet: Packet): Int = when (packet) {
            is Literal -> packet.version
            is Operator -> packet.version +
                    packet.subPackets.sumOf { sumVersionNumbers(it) }
        }

        println(input)
        return sumVersionNumbers(packet)
    }

    fun part2(input: String): Long {
        val packet = Parser(input).parsePacket()
        println(input)
        return packet.eval()

    }

    val input = readInput("Day16").first()

    println(part1("D2FE28"))
    println(part1("38006F45291200"))
    println(part1("EE00D40C823060"))
    println(part1("8A004A801A8002F478"))
    println(part1("620080001611562C8802118E34"))
    println(part1("C0015000016115A2E0802F182340"))
    println(part1("A0016C880162017C3686B18A3D4780"))
    println(part1(input))

    println()

    println(part2("C200B40A82"))
    println(part2("04005AC33890"))
    println(part2("880086C3E88112"))
    println(part2("CE00C43D881120"))
    println(part2("D8005AC2A8F0"))
    println(part2("F600BC2D8F"))
    println(part2("9C005AC2F8F0"))
    println(part2("9C0141080250320F1802104A08"))
    println(part2(input))
}
