package it.mi0772.keagle.hash;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Hasher implements Hasher {

    private MD5Hasher() {
    }

    private static volatile Hasher instance;
    public static Hasher getInstance() {
        if (instance == null) {
            synchronized (MD5Hasher .class) {
                if (instance == null) {
                    instance = new MD5Hasher();
                }
            }
        }
        return instance;
    }

    public String toHex(byte[] input) {
        StringBuilder result = new StringBuilder();
        for (byte b : calculate(input)) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
    public String toHex(String input) {
        return toHex(input.getBytes(StandardCharsets.UTF_8));
    }

    public byte[] calculate(byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(input);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] calculate(String input) {
        return calculate(input.getBytes(StandardCharsets.UTF_8));
    }
}
