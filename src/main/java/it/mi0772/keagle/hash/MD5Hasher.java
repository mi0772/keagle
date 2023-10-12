package it.mi0772.keagle.hash;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class MD5Hasher {

    private MD5Hasher() {
    }

    public static String toHex(byte[] input) {
        StringBuilder result = new StringBuilder();
        for (byte b : calculate(input)) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
    public static String toHex(String input) {
        return toHex(input.getBytes(StandardCharsets.UTF_8));
    }

    public static String toB64(byte[] input) {
        return Base64.getEncoder().encodeToString(calculate(input));
    }

    public static String toB64(String input) {
        return Base64.getEncoder().encodeToString(calculate(input));
    }

    public static byte[] calculate(byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(input);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] calculate(String input) {
        return calculate(input.getBytes(StandardCharsets.UTF_8));
    }
}
