import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.security.SecureRandom

fun sha256(input2: String): ByteArray {
    val salt = ByteArray(16)
    SecureRandom().nextBytes(salt)
    var input = input2.toByteArray() +salt

    val k = intArrayOf(
        0x428a2f98, 0x71374491, 0xb5c0fbcf.toInt(), 0xe9b5dba5.toInt(), 0x3956c25b, 0x59f111f1,
        0x923f82a4.toInt(), 0xab1c5ed5.toInt(),
        0xd807aa98.toInt(), 0x12835b01, 0x243185be, 0x550c7dc3, 0x72be5d74, 0x80deb1fe.toInt(), 0x9bdc06a7.toInt(),
        0xc19bf174.toInt(),
        0xe49b69c1.toInt(), 0xefbe4786.toInt(), 0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,
        0x983e5152.toInt(), 0xa831c66d.toInt(),
        0xb00327c8.toInt(), 0xbf597fc7.toInt(), 0xc6e00bf3.toInt(), 0xd5a79147.toInt(), 0x06ca6351, 0x14292967,
        0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb, 0x81c2c92e.toInt(), 0x92722c85.toInt(),
        0xa2bfe8a1.toInt(), 0xa81a664b.toInt(), 0xc24b8b70.toInt(), 0xc76c51a3.toInt(),
        0xd192e819.toInt(), 0xd6990624.toInt(), 0xf40e3585.toInt(), 0x106aa070,
        0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3,
        0x748f82ee, 0x78a5636f, 0x84c87814.toInt(), 0x8cc70208.toInt(),
        0x90befffa.toInt(), 0xa4506ceb.toInt(), 0xbef9a3f7.toInt(), 0xc67178f2.toInt()
    )
    // Početni ulazni bafer.
    val h = intArrayOf(
        0x6a09e667, 0xbb67ae85.toInt(), 0x3c6ef372, 0xa54ff53a.toInt(), 0x510e527f,
        0x9b05688c.toInt(), 0x1f83d9ab, 0x5be0cd19
    )
    fun rotirajDesno(value: Int, bits: Int): Int {
        return (value ushr bits) or (value shl (32 - bits))
    }
    fun pad(input: ByteArray): ByteArray {
        val paddingSize = (64 - (input.size + 9) % 64) % 64
        val padded = ByteBuffer.allocate(input.size + 1 + paddingSize + 8).order(ByteOrder.BIG_ENDIAN)
        padded.put(input)
        padded.put(0x80.toByte())
        padded.position(padded.capacity() - 8)
        padded.putLong(input.size.toLong() * 8)
        return padded.array()
    }

    val paddedInput = pad(input)
    val chunkCount = paddedInput.size / 64 //BROJ BLOKOVA

    for (i in 0 until chunkCount) {
        val chunk = paddedInput.copyOfRange(i * 64, (i + 1) * 64)
        val w = IntArray(64)

        for (j in 0 until 16) {
            w[j] = ByteBuffer.wrap(chunk, j * 4, 4).order(ByteOrder.BIG_ENDIAN).int
        }

        for (j in 16 until 64) {
            val s0 = rotirajDesno(w[j - 15], 7) xor rotirajDesno(w[j - 15], 18) xor (w[j - 15] ushr 3)
            val s1 = rotirajDesno(w[j - 2], 17) xor rotirajDesno(w[j - 2], 19) xor (w[j - 2] ushr 10)
            w[j] = w[j - 16] + s0 + w[j - 7] + s1
        }

        var a = h[0]; var b = h[1]; var c = h[2]; var d = h[3]; var e = h[4]; var f = h[5]; var g = h[6];var h0 = h[7]

        for (j in 0 until 64) {
            val s1 = rotirajDesno(e, 6) xor rotirajDesno(e, 11) xor rotirajDesno(e, 25)
            val ch = (e and f) xor ((e.inv()) and g)
            val temp1 = h0 + s1 + ch + k[j] + w[j]
            val s0 = rotirajDesno(a, 2) xor rotirajDesno(a, 13) xor rotirajDesno(a, 22)
            val maj = (a and b) xor (a and c) xor (b and c)
            val temp2 = s0 + maj

            h0 = g; g = f; f = e; e = d + temp1; d = c; c = b; b = a; a = temp1 + temp2
        }

        h[0] += a; h[1] += b; h[2] += c; h[3] += d; h[4] += e; h[5] += f; h[6] += g; h[7] += h0
    }

    val result = ByteBuffer.allocate(32).order(ByteOrder.BIG_ENDIAN)
    for (i in h) {
        result.putInt(i)
    }

    return result.array()
}

fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }
//SHA-256 hash2: 2cafd27332c78a8cc657c088af0ce20d5a15284928ccf52e5626b4f6481e7884
fun main() {
    val hash = sha256("Hash-ovana poruka")
    val hash2 = sha256("Hash-ovana poruka")
    println("SHA-256 hash: ${hash.toHexString()}")
    println("SHA-256 hash2: ${hash2.toHexString()}")
}
