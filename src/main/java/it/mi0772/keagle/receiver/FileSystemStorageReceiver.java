package it.mi0772.keagle.receiver;

import it.mi0772.keagle.exceptions.KRecordAlreadyExists;
import it.mi0772.keagle.filesystem.Resource;
import it.mi0772.keagle.hash.MD5Hasher;
import it.mi0772.keagle.record.KRecord;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

public class FileSystemStorageReceiver implements StorageReceiver {
    @Override
    public KRecord put(String key, byte[] value) throws KRecordAlreadyExists, IOException {
        var resource = new Resource(MD5Hasher.toHex(key));
        if (resource.exist())
            throw new KRecordAlreadyExists("record with key "+key+" already exist");

        var path = resource.create();
        resource.store(path, value);

        return new KRecord(key, value, Instant.now(), null);
    }

    @Override
    public Optional<KRecord> get(String key) throws IOException {
        var resource = new Resource(MD5Hasher.toHex(key));
        if (!resource.exist()) {
            return Optional.empty();
        }

        byte[] content = resource.read();

        return Optional.of(new KRecord(key, content, null, null));
    }
}
