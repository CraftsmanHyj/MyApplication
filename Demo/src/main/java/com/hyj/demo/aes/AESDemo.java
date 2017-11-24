package com.hyj.demo.aes;

import android.util.Base64;

import com.hyj.lib.tools.LogUtils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


/**
 * https://github.com/fred-ye/summary/issues/47#event-312555162
 * Created by Administrator on 2017/4/25.
 */

public class AESDemo {
    public static byte[] encrypt(byte[] key, byte[] data) {
        SecretKeySpec sKeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher;
        byte[] encryptedData = null;
        try {
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, sKeySpec);
            encryptedData = cipher.doFinal(data);
        } catch (Exception e) {
            LogUtils.i("encrypt exception" + e.getMessage());
        }
        return encryptedData;
    }

    public static byte[] decrypt(byte[] key, byte[] data) {
        SecretKeySpec sKeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher;
        byte[] decryptedData = null;
        try {
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec);
            decryptedData = cipher.doFinal(data);
        } catch (Exception e) {
            LogUtils.i("decrypt exception" + e.getMessage());
        }
        return decryptedData;
    }

    public static byte[] generateKey(byte[] randomNumberSeed) {
        SecretKey sKey = null;
        KeyGenerator keyGen;
        try {
            keyGen = KeyGenerator.getInstance("AES");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(randomNumberSeed);
            sKey = keyGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            LogUtils.i("generateKey exception" + e.getMessage());
        }
        return sKey.getEncoded();
    }

    //调用的demo
    public static void testAES() {
        String randomNumberSeed = "aaaa";
        String data = "Hello World";
        byte[] key = generateKey(randomNumberSeed.getBytes());
        byte[] encryptData = encrypt(key, data.getBytes());
        String str = Base64.encodeToString(encryptData, Base64.DEFAULT);
        LogUtils.i("encrypt data:" + str);
        byte[] decodeData = Base64.decode(str, Base64.DEFAULT);
        LogUtils.i("encrypt data:" + new String(decrypt(key, decodeData)));
    }
}
