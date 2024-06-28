import java.security.MessageDigest

fun md5(input: String): String {
    val bytes = MessageDigest.getInstance("MD5").digest(input.toByteArray())
    return bytes.joinToString("") {
        "%02x".format(it)
    }
}

fun main() {
    val input = "hello world"
    val hash = md5(input)
    println("MD5 hash od '$input' je: $hash")
}

5eb63bbbe01eeed093cb22bb8f5acdc3
5eb63bbbe01eeed093cb22bb8f5acdc3