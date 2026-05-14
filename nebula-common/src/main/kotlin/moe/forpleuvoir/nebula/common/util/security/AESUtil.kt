package moe.forpleuvoir.nebula.common.util.security

import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

@Suppress("unused")
object AESUtil {

    private const val ALGORITHM = "AES/CBC/PKCS5Padding"
    private const val IV_SIZE = 16

    fun encrypt(plaintext: String, key: String): String {
        val cipher = Cipher.getInstance(ALGORITHM)
        val keySpec = SecretKeySpec(key.toByteArray(Charsets.UTF_8), "AES")
        val iv = ByteArray(IV_SIZE)
        SecureRandom().nextBytes(iv)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, IvParameterSpec(iv))
        val encrypted = cipher.doFinal(plaintext.toByteArray(charset("UTF-8")))
        val combined = ByteArray(iv.size + encrypted.size)
        System.arraycopy(iv, 0, combined, 0, iv.size)
        System.arraycopy(encrypted, 0, combined, iv.size, encrypted.size)
        return Base64.getEncoder().encodeToString(combined)
    }

    fun decrypt(ciphertext: String, key: String): String {
        val cipher = Cipher.getInstance(ALGORITHM)
        val keySpec = SecretKeySpec(key.toByteArray(Charsets.UTF_8), "AES")
        val combined = Base64.getDecoder().decode(ciphertext)
        val iv = combined.copyOfRange(0, IV_SIZE)
        val encrypted = combined.copyOfRange(IV_SIZE, combined.size)
        cipher.init(Cipher.DECRYPT_MODE, keySpec, IvParameterSpec(iv))
        val decrypted = cipher.doFinal(encrypted)
        return String(decrypted, charset("UTF-8"))
    }
}