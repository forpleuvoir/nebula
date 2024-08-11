package moe.forpleuvoir.nebula.common.util.security

import java.security.*
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher


object RSAUtil {

    data class KeyPair(val publicKey: String, val privateKey: String)

    /**
     * 随机生成密钥对
     * @throws NoSuchAlgorithmException
     */
    @Throws(Exception::class)
    fun getKeyPair(): KeyPair {
        //KeyPairGenerator类用于生成公钥和密钥对，基于RSA算法生成对象
        val keyPairGen: KeyPairGenerator = KeyPairGenerator.getInstance("RSA")
        //初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024, SecureRandom())
        //生成一个密钥对，保存在keyPair中
        val keyPair: java.security.KeyPair = keyPairGen.generateKeyPair()
        val privateKey: PrivateKey = keyPair.private //得到私钥
        val publicKey: PublicKey = keyPair.public //得到公钥
        //得到公钥字符串
        val publicKeyString = String(Base64.getEncoder().encode(publicKey.encoded))
        //得到私钥字符串
        val privateKeyString = String(Base64.getEncoder().encode(privateKey.encoded))
        return KeyPair(publicKeyString, privateKeyString)
    }

    /**
     * RSA公钥加密
     *
     * @param str
     * 加密字符串
     * @param publicKey
     * 公钥
     * @return 密文
     * @throws Exception
     * 加密过程中的异常信息
     */
    @Throws(Exception::class)
    fun encrypt(str: String, publicKey: String): String {
        //base64编码的公钥
        val decoded: ByteArray = Base64.getDecoder().decode(publicKey)
        val pubKey: RSAPublicKey = KeyFactory.getInstance("RSA").generatePublic(X509EncodedKeySpec(decoded)) as RSAPublicKey
        //RAS加密
        val cipher: Cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.ENCRYPT_MODE, pubKey)
        return Base64.getEncoder().encodeToString(cipher.doFinal(str.toByteArray(charset("UTF-8"))))
    }

    /**
     * RSA私钥解密
     *
     * @param str
     * 加密字符串
     * @param privateKey
     * 私钥
     * @return 铭文
     * @throws Exception
     * 解密过程中的异常信息
     */
    @Throws(Exception::class)
    fun decrypt(str: String, privateKey: String): String {
        //Base64解码加密后的字符串
        val inputByte: ByteArray = Base64.getDecoder().decode(str.toByteArray(charset("UTF-8")))
        //Base64编码的私钥
        val decoded: ByteArray = Base64.getDecoder().decode(privateKey)
        val priKey: PrivateKey = KeyFactory.getInstance("RSA").generatePrivate(PKCS8EncodedKeySpec(decoded))
        //RSA解密
        val cipher: Cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.DECRYPT_MODE, priKey)
        return String(cipher.doFinal(inputByte))
    }
}