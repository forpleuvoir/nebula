import com.forpleuvoir.nebula.common.times
import com.forpleuvoir.nebula.common.util.RSAUtil

fun main() {
	test1()
}


fun test1() {
	times {
		val keyPair = RSAUtil.getKeyPair()
		val publicKey = keyPair.publicKey
		val privateKey = keyPair.privateKey
		println(keyPair)
		val ciphertext = RSAUtil.encrypt("原文123456", publicKey)
		println("密文 ：$ciphertext")
		val plainText = RSAUtil.decrypt(ciphertext, privateKey)
		println("解密 ：$plainText")
	}
}