package it.mi0772.keagle.storage;

import it.mi0772.keagle.hash.HasherFactory;

import java.util.Optional;

public class Item {
    private final String key;
    private final String hashedKey;
    private byte[] value;

    public Item(String key) {
        this.key = key;
        this.hashedKey = HasherFactory.getDefaultHasher().toHex(key);
    }

    public Item(String key, byte[] value) {
        this(key);
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getHashedKey() {
        return hashedKey;
    }

    public Optional<byte[]> getValue() {
        return Optional.ofNullable(this.value);
    }
}
