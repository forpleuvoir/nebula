package moe.forpleuvoir.nebula.common.util

import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

@Suppress("unused")
object AESUtil {

	fun encrypt(plaintext: String, key: String): String {
		//创建cipher对象
		val cipher = Cipher.getInstance("AES")
		//初始化cipher
		//通过秘钥工厂生产秘钥
		val keySpec = SecretKeySpec(key.toByteArray(), "AES")
		cipher.init(Cipher.ENCRYPT_MODE, keySpec)
		//加密、解密
		val encrypt = cipher.doFinal(plaintext.toByteArray())
		return String(Base64.getEncoder().encode(encrypt))
	}

	fun decrypt(ciphertext: String, key: String): String {
		//创建cipher对象
		val cipher = Cipher.getInstance("AES")
		//初始化cipher
		//通过秘钥工厂生产秘钥
		val keySpec = SecretKeySpec(key.toByteArray(), "AES")
		cipher.init(Cipher.DECRYPT_MODE, keySpec)
		//加密、解密
		val encrypt = cipher.doFinal(Base64.getDecoder().decode(ciphertext))
		return String(encrypt)
	}


}