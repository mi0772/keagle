package it.mi0772.keagle.hash;

public interface Hasher {
    byte[] calculate(byte[] input);
    byte[] calculate(String input);
    String toHex(String input);
    String toHex(byte[] input);
}
