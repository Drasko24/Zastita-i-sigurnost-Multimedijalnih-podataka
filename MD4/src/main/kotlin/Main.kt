import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.MessageDigest
import java.security.Security
fun main() {
    Security.addProvider(BouncyCastleProvider())
    val inputString = "Poruka/Password poslat od strane korisnika."
    val hash = md4Hash(inputString)
    println("Hashovana vrijednost poslate poruke/passworda:")
    println(hash)
}
fun md4Hash(input: String): String {
    val md = MessageDigest.getInstance("MD4")
    val bytes = md.digest(input.toByteArray())
    return bytes.joinToString("") { "%02x".format(it) }
}
