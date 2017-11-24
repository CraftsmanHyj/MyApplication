package com.hyj.demo.aes;

import android.util.Base64;

import java.nio.charset.Charset;
import java.security.Key;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES密钥算法
 * 需要bcprov-ext-jdk15on-151.jar 的支持
 * Created by apple_half on 15-1-20.
 */
public class AesUtils {
    /**
     * 密钥算法
     */
    private static final String KEY_ALGORITHM = "AES";

    /**
     * 位移量，可能必须是16位
     */
    public final static String Encryption_IV = "9mg+!7ed8b36*w`X";
    /**
     * AES/CBC/PKCS7Padding 分别对应
     * 加密：解密算法、工作模式、填充方式
     */
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";

    /**
     * 字符编码方式
     */
    private static final String CHARSET_NAME = "UTF-8";

    /**
     * 生成密钥，可以由指定字符串通过计算之后生成密钥
     *
     * @return
     * @throws Exception
     */
    private static byte[] getKey() throws Exception {
        return AESMainActivity.Encryption_SecretKey.getBytes(Charset.forName(CHARSET_NAME));
    }

    /**
     * 加密数据, 返回字节数组
     *
     * @param toEncrypt
     * @return
     * @throws Exception
     */
    public static byte[] encrypt2Byte(String toEncrypt) throws Exception {
        Key secretKey = new SecretKeySpec(getKey(), KEY_ALGORITHM);

        // libs 中 bcprov-ext-jdk15on-151.jar 的支持, bouncycastle 支持 64 位密钥
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);

        // 获取位移，并初始化
        final byte[] ivData = Encryption_IV.getBytes();
        IvParameterSpec iv = new IvParameterSpec(ivData);

        // 用 iv 初始化
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

        final byte[] encryptedMessage = cipher.doFinal(toEncrypt.getBytes(Charset.forName(CHARSET_NAME)));

        final byte[] ivAndEncryptedMessage = new byte[ivData.length + encryptedMessage.length];
        System.arraycopy(ivData, 0, ivAndEncryptedMessage, 0, ivData.length);
        System.arraycopy(encryptedMessage, 0, ivAndEncryptedMessage, ivData.length, encryptedMessage.length);

        return ivAndEncryptedMessage;
    }

    /**
     * 加密数据，返回 String 对象
     *
     * @param toEncrypt
     * @return
     * @throws Exception
     */
    public static String encrypt2Str(String toEncrypt) throws Exception {
        byte[] ivAndEncryptedMessage = encrypt2Byte(toEncrypt);
        String strResult = new String(Base64.encode(ivAndEncryptedMessage, 0), CHARSET_NAME);
        return strResult;
    }

    /**
     * 数据解密
     *
     * @param toDecrypt
     * @return
     * @throws Exception
     */
    public static String decrypt(String toDecrypt) throws Exception {
        byte[] data = Base64.decode(toDecrypt, 0);

        Key secretKey = new SecretKeySpec(getKey(), KEY_ALGORITHM);

        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        final byte[] ivData = Encryption_IV.getBytes();
        IvParameterSpec iv = new IvParameterSpec(ivData);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        final byte[] encryptedMessage = cipher.doFinal(data);

        final byte[] result = new byte[encryptedMessage.length - ivData.length];
        System.arraycopy(encryptedMessage, ivData.length, result, 0, result.length);
        return new String(result);
    }
}