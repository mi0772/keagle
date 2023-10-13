package it.mi0772.keagle.hash;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA3Hasher implements Hasher {

    private SHA3Hasher() {
    }

    private static volatile Hasher instance;
    public static Hasher getInstance() {
        if (instance == null) {
            synchronized (MD5Hasher .class) {
                if (instance == null) {
                    instance = new SHA3Hasher();
                }
            }
        }
        return instance;
    }

    public String toHex(byte[] input) {
        return DatatypeConverter.printHexBinary(calculate(input));
    }
    public String toHex(String input) {
        return toHex(input.getBytes(StandardCharsets.UTF_8));
    }

    public byte[] calculate(byte[] input) {
        try {
            MessageDigest sha3 = MessageDigest.getInstance("SHA3-224");
            return sha3.digest(input);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] calculate(String input) {
        return calculate(input.getBytes(StandardCharsets.UTF_8));
    }
}
