package example.thiyagu.com.cert;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AesEnc {

    //private static String algorithm = "AES";
    private static String algorithm = "AES";
    private static byte[] decode(String textString) {
        byte[] byteArray = new byte[(textString.length() / 2)];
        int intVal = 0;
        String frag = "";
        int c1 = 0;
        for (int i = 0; i < byteArray.length; i++) {
            c1 = (i * 2);
            frag = textString.substring(c1, (c1 + 2));
            intVal = Integer.parseInt(frag, 16);
            byteArray[i] = (byte) (0xff & intVal);
        }
        return byteArray;
    }

    private static Key generateKey(byte[] keyValue) throws Exception {
        Key key = new SecretKeySpec(keyValue, algorithm);
        return key;
    }

    public static String decrypt(String encryptedText,byte[] keyValue) throws Exception {
        // generate key
        Key key = generateKey(keyValue);
        Cipher chiper = Cipher.getInstance(algorithm);
        chiper.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = decode(encryptedText);
        byte[] decValue = chiper.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }
}
