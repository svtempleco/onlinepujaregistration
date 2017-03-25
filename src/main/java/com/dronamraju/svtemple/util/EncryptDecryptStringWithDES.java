package com.dronamraju.svtemple.util;

/**
 * Created by mdronamr on 3/23/17.
 */



import com.sun.mail.util.BASE64DecoderStream;
import com.sun.mail.util.BASE64EncoderStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import sun.misc.BASE64Encoder;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;

public class EncryptDecryptStringWithDES {

    private static Log log = LogFactory.getLog(EncryptDecryptStringWithDES.class);
    //private static Cipher ecipher;
    //private static Cipher dcipher;
    //private static SecretKey key;

    private static final String UNICODE_FORMAT = "UTF8";
    public static final String DES_ENCRYPTION_SCHEME = "DES";
    private static String myEncryptionKey = "ThisIsSecretEncryptionKey";


    /*
    public static String encrypt(String str) {
        try {
            byte[] keyAsBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
            KeySpec myKeySpec = new DESKeySpec(keyAsBytes);
            SecretKeyFactory mySecretKeyFactory = SecretKeyFactory.getInstance(DES_ENCRYPTION_SCHEME);
            Cipher cipher = Cipher.getInstance(DES_ENCRYPTION_SCHEME);
            SecretKey key = mySecretKeyFactory.generateSecret(myKeySpec);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] plainText = str.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = cipher.doFinal(plainText);
            BASE64Encoder base64encoder = new BASE64Encoder();
            String encrypted = base64encoder.encode(encryptedText);
            log.info("encrypted: " + encrypted);
            return encrypted;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    */

    /*
    public static String decrypt(String str) {
        try {
            byte[] keyAsBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
            KeySpec myKeySpec = new DESKeySpec(keyAsBytes);
            SecretKeyFactory mySecretKeyFactory = SecretKeyFactory.getInstance(DES_ENCRYPTION_SCHEME);
            Cipher cipher = Cipher.getInstance(DES_ENCRYPTION_SCHEME);
            SecretKey key = mySecretKeyFactory.generateSecret(myKeySpec);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] encryptedText = cipher.doFinal(str.getBytes());
            BASE64Encoder base64encoder = new BASE64Encoder();
            String decrypted = base64encoder.encode(encryptedText);
            log.info("decrypted: " + decrypted);
            return decrypted;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    */
}
