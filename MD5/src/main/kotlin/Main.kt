import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.abs
import kotlin.math.sin

val s = intArrayOf(
    7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22,
    5, 9, 14, 20, 5, 9, 14, 20, 5, 9, 14, 20, 5, 9, 14, 20,
    4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23,
    6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21
)
val K = IntArray(64) { i -> (abs(sin(i + 1.0)) * (1L shl 32)).toLong().toInt() }
fun rotirajLijevo(x: Int, c: Int): Int {
    return (x shl c) or (x ushr (32 - c))
}
fun md5(poruka2: String): String {
    var poruka = poruka2.toByteArray()
    var message = poruka
    val duzinaUbitima = (8L * message.size).toLong()

    message += byteArrayOf(0x80.toByte())
    while (message.size % 64 != 56) {
        message += byteArrayOf(0)
    }
    val lenBytes = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(duzinaUbitima).array()
    message += lenBytes

    var h0 = 0x67452301; var h1 = 0xEFCDAB89.toInt(); var h2 = 0x98BADCFE.toInt(); var h3 = 0x10325476

    for (i in message.indices step 64) {
        val blok = message.sliceArray(i until i + 64)
        val M = ByteBuffer.wrap(blok).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer().run {
            IntArray(16) { get(it) }
        }
        var a = h0; var b = h1;  var c = h2; var d = h3

        for (j in 0 until 64) {
            val f: Int
            val g: Int
            when (j) {
                in 0..15 -> {
                    f = (b and c) or (b.inv() and d)
                    g = j
                }
                in 16..31 -> {
                    f = (d and b) or (d.inv() and c)
                    g = (5 * j + 1) % 16
                }
                in 32..47 -> {
                    f = b xor c xor d
                    g = (3 * j + 5) % 16
                }
                else -> {
                    f = c xor (b or d.inv())
                    g = (7 * j) % 16
                }
            }
            val temp = d
            d = c
            c = b
            b = b + rotirajLijevo(a + f + K[j] + M[g], s[j])
            a = temp
        }
        h0 += a; h1 += b; h2 += c; h3 += d
    }
    return ByteBuffer.allocate(16).order(ByteOrder.LITTLE_ENDIAN)
        .putInt(h0).putInt(h1).putInt(h2).putInt(h3).array().joinToString("") { "%02x".format(it) }
}
fun main() {
    val poruka = "Hash-ovana poruka"
    println("MD5 hash je: ${md5(poruka)}")
}
