package it.mi0772.keagle.record;

import it.mi0772.keagle.hash.HasherFactory;

import java.time.Instant;

public class KRecord  {
    private final String key;
    private final byte[] value;
    private final Instant creationTime;
    private final Instant expiresAt;
    private final String valueHash;
    private final String keyHash;

    public KRecord(String key, byte[] value, Instant creationTime, Instant expiresAt) {
        this.key = key;
        this.value = value;
        this.creationTime = creationTime;
        this.expiresAt = expiresAt;
        this.valueHash = HasherFactory.getDefaultHasher().toHex(this.value);
        this.keyHash   = HasherFactory.getDefaultHasher().toHex(this.key);
    }

    public String getKey() {
        return key;
    }

    public byte[] getValue() {
        return value;
    }

    public Instant getCreationTime() {
        return creationTime;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public String getValueHash() {
        return valueHash;
    }

    public String getKeyHash() {
        return keyHash;
    }

    @Override
    public String toString() {
        return "KRecord{" +
                "key='" + key + '\'' +
                ", creationTime=" + creationTime +
                ", expiresAt=" + expiresAt +
                ", valueHash='" + valueHash + '\'' +
                ", keyHash='" + keyHash + '\'' +
                '}';
    }
}
