
package Modelo;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;


public class Hash {
    
    public static String sha256(char[] password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(new String(password).getBytes(StandardCharsets.UTF_8));
            BigInteger number = new BigInteger(1, encodedhash);
            return number.toString(16);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
}
}
