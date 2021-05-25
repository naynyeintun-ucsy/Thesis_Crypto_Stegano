package com.whk.spa.core;

import android.util.Log;

import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Naylay on 1/9/2018.
 */


public class AES{
    public byte [] encryptedText;
    private static String algorithm = "AES";
    private static byte[] keyValue = new byte[15];
    static Hashgen hash = new Hashgen();

    // Performs Encryption
    public  byte[] encrypt(byte[] plainText) throws Exception {

        Key key = generateKey();
        Cipher chiper = Cipher.getInstance(algorithm);
        chiper.init(Cipher.ENCRYPT_MODE, key);

        byte[] encVal = chiper.doFinal(plainText);

        return encVal;
    }

    // Performs decryption
    public  byte[] decrypt(byte[] encryptedText) throws Exception {
        // generate key
        Key key = generateKey();
        Cipher chiper = Cipher.getInstance(algorithm);
        chiper.init(Cipher.DECRYPT_MODE, key);
       // decode()
        byte[] decordedValue = encryptedText;
        byte[] decValue = chiper.doFinal(decordedValue);
      //  String decryptedValue = new String(decValue);
        return decValue;
    }

    // generateKey() is used to generate a secret key for AES algorithm
    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, algorithm);
        return key;
    }

    // performs encryption & decryption
   public AES(String inputkey){

       // String plainText = "This ";
      try {
          // This is the variable i am passing to MainActivity
          keyValue = hash.keyGeneartor(inputkey);

      }catch (NoSuchAlgorithmException ensh)
      {
          Log.e("error my ensh", String.valueOf(ensh));

      }catch (Exception e)
      {
          Log.e("error my e", String.valueOf(e));

      }

    }


}