package it.mi0772.keagle.filesystem;

import it.mi0772.keagle.enums.StorageType;

import java.time.Duration;
import java.time.LocalDateTime;

public class Entry {
    private String key;
    private LocalDateTime expire;
    private byte[] content;
    private StorageType storageType;

    public Entry(StorageType storageType, String key, LocalDateTime expire) {
        this.key = key;
        this.expire = expire;
        this.storageType = storageType;
    }

    public Entry(StorageType storageType, String key) {
        this.key = key;
        this.expire = null;
        this.storageType = storageType;
    }

    public Entry(String key) {
        this.key = key;
        this.expire = null;
        this.storageType = null;
    }

    public Entry(StorageType storageType, String key, Duration duration) {
        this.key = key;
        this.storageType = storageType;
        if (duration != null)
            this.expire = LocalDateTime.now().plus(duration);
    }

    public String getKey() {
        return key;
    }

    public LocalDateTime getExpire() {
        return expire;
    }

    public byte[] getContent() {
        return content;
    }

    public StorageType getStorageType() {
        return storageType;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
