package com.itouch8.pump.core.util.rsa;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.itouch8.pump.core.util.rsa.impl.KeyProperty;

/**
 * @author Kingdom
 * @date 2016-1-8
 * @version 1.0
 * @description RSA辅助类
 */
public class RSA {

    private RSA() {}

    private static RSA rsa = null;

    private static int KEY_SIZE = 512;

    private static String RSA = "RSA";

    public static RSA create() {
        if (null == rsa) {
            synchronized (RSA.class) {
                if (null == rsa) {
                    rsa = new RSA();
                }
            }
        }
        return rsa;
    }

    /**
     * * 生成密钥对 *
     * 
     * @return KeyPair *
     * @throws EncryptException
     */
    public KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(RSA, new BouncyCastleProvider());
        keyPairGen.initialize(KEY_SIZE, new SecureRandom());
        return keyPairGen.generateKeyPair();
    }

    /**
     * * 生成公钥 *
     * 
     * @param modulus *
     * @param publicExponent *
     * @return RSAPublicKey *
     * @throws Exception
     */
    public RSAPublicKey generateRSAPublicKey(BigInteger modulus, BigInteger publicExponent) throws Exception {
        KeyFactory keyFac = KeyFactory.getInstance(RSA, new BouncyCastleProvider());
        RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(modulus, publicExponent);
        return (RSAPublicKey) keyFac.generatePublic(pubKeySpec);
    }

    public RSAPublicKey generateRSAPublicKey(KeyProperty kp) throws Exception {
        KeyFactory keyFac = KeyFactory.getInstance(RSA, new BouncyCastleProvider());
        RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger(kp.getModule()), new BigInteger(kp.getPublicEmpoent()));
        return (RSAPublicKey) keyFac.generatePublic(pubKeySpec);
    }

    /**
     * * 生成私钥 *
     * 
     * @param modulus *
     * @param privateExponent *
     * @return RSAPrivateKey *
     * @throws Exception
     */
    public RSAPrivateKey generateRSAPrivateKey(BigInteger modulus, BigInteger privateExponent) throws Exception {
        KeyFactory keyFac = KeyFactory.getInstance(RSA, new BouncyCastleProvider());
        RSAPrivateKeySpec priKeySpec = new RSAPrivateKeySpec(modulus, privateExponent);
        return (RSAPrivateKey) keyFac.generatePrivate(priKeySpec);
    }

    /**
     * * 加密 *
     * 
     * @param key 加密的密钥 *
     * @param data 待加密的明文数据 *
     * @return 加密后的数据 *
     * @throws Exception
     */
    public byte[] encrypt(PublicKey pk, byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA, new BouncyCastleProvider());
        cipher.init(Cipher.ENCRYPT_MODE, pk);
        int blockSize = cipher.getBlockSize();// 获得加密块大小，如：加密前数据为128个byte，而key_size=1024
        // 加密块大小为127
        // byte,加密后为128个byte;因此共有2个加密块，第一个127
        // byte第二个为1个byte
        int outputSize = cipher.getOutputSize(data.length);// 获得加密块加密后块大小
        int leavedSize = data.length % blockSize;
        int blocksSize = leavedSize != 0 ? data.length / blockSize + 1 : data.length / blockSize;
        byte[] raw = new byte[outputSize * blocksSize];
        int i = 0;
        while (data.length - i * blockSize > 0) {
            if (data.length - i * blockSize > blockSize)
                cipher.doFinal(data, i * blockSize, blockSize, raw, i * outputSize);
            else
                cipher.doFinal(data, i * blockSize, data.length - i * blockSize, raw, i * outputSize);
            // 这里面doUpdate方法不可用，查看源代码后发现每次doUpdate后并没有什么实际动作除了把byte[]放到
            // ByteArrayOutputStream中，而最后doFinal的时候才将所有的byte[]进行加密，可是到了此时加密块大小很可能已经超出了
            // OutputSize所以只好用dofinal方法。
            i++;
        }
        return raw;
    }

    /**
     * * 解密 *
     * 
     * @param key 解密的密钥 *
     * @param raw 已经加密的数据 *
     * @return 解密后的明文 *
     * @throws Exception
     */
    public byte[] decrypt(PrivateKey pk, byte[] raw) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA, new BouncyCastleProvider());
        cipher.init(Cipher.DECRYPT_MODE, pk);
        int blockSize = cipher.getBlockSize();
        ByteArrayOutputStream bout = new ByteArrayOutputStream(64);
        int j = 0;
        while (raw.length - j * blockSize > 0) {
            bout.write(cipher.doFinal(raw, j * blockSize, blockSize));
            j++;
        }
        return bout.toByteArray();
    }

    /**
     * * 解密 *
     * 
     * @param key 解密的密钥 *
     * @param raw 已经加密的数据 *
     * @return 解密后的明文 *
     * @throws Exception
     */
    public String decryptString(PrivateKey pk, byte[] raw, String charset) throws Exception {

        byte[] b = decrypt(pk, raw);
        return new String(b, charset);
    }

    public String decryptString(PrivateKey pk, byte[] raw) throws Exception {

        byte[] b = decrypt(pk, raw);
        return new String(b, "utf-8");
    }

    public String decryptString(KeyProperty kp, byte[] raw, String charset) throws Exception {

        RSAPrivateKey privateKey = generateRSAPrivateKey(new BigInteger(kp.getModule()), new BigInteger(kp.getPrivateEmpoent()));
        byte[] b = decrypt(privateKey, raw);
        return new String(b, charset);
    }

    public String decryptString(KeyProperty kp, byte[] raw) throws Exception {

        RSAPrivateKey privateKey = generateRSAPrivateKey(new BigInteger(kp.getModule()), new BigInteger(kp.getPrivateEmpoent()));
        byte[] b = decrypt(privateKey, raw);
        return new String(b, "utf-8");
    }

    public String encryptString(KeyProperty kp, byte[] raw, String charset) throws Exception {

        RSAPublicKey generateRSAPublicKey = generateRSAPublicKey(kp);
        byte[] b = encrypt(generateRSAPublicKey, raw);
        return new String(b, charset);
    }

    public String encryptString(KeyProperty kp, byte[] raw) throws Exception {

        RSAPublicKey generateRSAPublicKey = generateRSAPublicKey(kp);
        byte[] b = encrypt(generateRSAPublicKey, raw);
        return new String(b, "utf-8");

    }

}
