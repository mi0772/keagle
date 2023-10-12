package it.mi0772.keagle.receiver;

import it.mi0772.keagle.exceptions.KRecordAlreadyExists;
import it.mi0772.keagle.filesystem.Resource;
import it.mi0772.keagle.hash.MD5Hasher;
import it.mi0772.keagle.record.KRecord;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

public class FileSystemStorageReceiver implements StorageReceiver {
    @Override
    public KRecord put(String namespace, String key, byte[] value, Duration duration) throws KRecordAlreadyExists, IOException {
        var resource = new Resource(namespace, MD5Hasher.toHex(key));
        if (resource.exist())
            throw new KRecordAlreadyExists("record with key "+key+" already exist");

        resource.store(value, duration);
        return new KRecord(key, value, Instant.now(), null);
    }

    @Override
    public Optional<KRecord> get(String namespace, String key) throws IOException {
        var resource = new Resource(namespace, MD5Hasher.toHex(key));
        if (!resource.exist() || resource.isExpired()) {
            return Optional.empty();
        }
        byte[] content = resource.read();
        return Optional.of(new KRecord(key, content, null, null));
    }
}
