fun isValid_generated(inputs: List<Int>): Boolean {
    var (w, x, z) = listOf(0, 0, 0)

    // push
    w = inputs[0]
    z += w + 7

    // push
    w = inputs[1]
    z *= 26
    z += w + 15

    // push
    w = inputs[2]
    z *= 26
    z += w + 2

    // pop
    w = inputs[3]
    x = (z % 26) - 3
    z /= 26
    x = if (x == w) 0 else 1
    z *= 25 * x + 1
    z += (w + 15) * x

    // push
    w = inputs[4]
    z *= 26
    z += w + 14

    // pop
    w = inputs[5]
    x = (z % 26) - 9
    z /= 26
    x = if (x == w) 0 else 1
    z *= 25 * x + 1
    z += (w + 2) * x

    // push
    w = inputs[6]
    z *= 26
    z += w + 15

    // pop
    w = inputs[7]
    x = (z % 26) - 7
    z /= 26
    x = if (x == w) 0 else 1
    z *= 25 * x + 1
    z += (w + 1) * x

    // pop
    w = inputs[8]
    x = (z % 26) - 11
    z /= 26
    x = if (x == w) 0 else 1
    z *= 25 * x + 1
    z += (w + 15) * x

    // pop
    w = inputs[9]
    x = (z % 26) - 4
    z /= 26
    x = if (x == w) 0 else 1
    z *= 25 * x + 1
    z += (w + 15) * x

    // push
    w = inputs[10]
    z *= 26
    z += w + 12

    // push
    w = inputs[11]
    z *= 26
    z += w + 2

    // pop
    w = inputs[12]
    x = (z % 26) - 8
    z /= 26
    x = if (x == w) 0 else 1
    z *= 25 * x + 1
    z += (w + 13) * x

    // pop
    w = inputs[13]
    x = (z % 26) - 10
    z /= 26
    x = if (x == w) 0 else 1
    z *= 25 * x + 1
    z += (w + 13) * x

    return z == 0
}
