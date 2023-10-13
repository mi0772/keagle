package it.mi0772.keagle.receiver;

import it.mi0772.keagle.config.KConfig;
import it.mi0772.keagle.enums.StorageType;
import it.mi0772.keagle.exceptions.EntryExpiredException;
import it.mi0772.keagle.exceptions.KRecordAlreadyExists;
import it.mi0772.keagle.filesystem.Entry;
import it.mi0772.keagle.filesystem.FSTree;
import it.mi0772.keagle.hash.Hasher;
import it.mi0772.keagle.hash.HasherFactory;
import it.mi0772.keagle.record.KRecord;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

public class FileSystemStorageReceiver implements StorageReceiver {

    private final FSTree fsTree;

    public FileSystemStorageReceiver() {
        this.fsTree = FSTree.getInstance();
    }

    @Override
    public KRecord put(String key, byte[] value, Duration duration, StorageType storageType) throws KRecordAlreadyExists, IOException {

        var entry = new Entry(storageType, HasherFactory.getDefaultHasher().toHex(key), duration);
        if (fsTree.find(HasherFactory.getDefaultHasher().toHex(key)) != null)
            throw new KRecordAlreadyExists("record with key "+key+" already exist");

        switch (storageType) {
            case FILESYSTEM -> {
                Path entryPath = Path.of(KConfig.getInstance().getProperty("STORAGE_PATH"), entry.getKey());
                Files.createFile(entryPath);
                Files.write(entryPath, value);
            }
            case MEMORY -> entry.setContent(value);
        }

        fsTree.insert(entry);
        return new KRecord(key, value, Instant.now(), entry.getExpire() != null ? entry.getExpire().toInstant(ZoneOffset.UTC) : null);
    }

    @Override
    public Optional<KRecord> get(String key) throws EntryExpiredException {

        var sEntry = fsTree.find(HasherFactory.getDefaultHasher().toHex(key));

        if (sEntry == null)
            return Optional.empty();

        if (sEntry.getValue().getExpire() != null && LocalDateTime.now().isAfter(sEntry.getValue().getExpire())) {
            fsTree.deleteNode(sEntry.getValue().getKey());
            throw new EntryExpiredException("Expired");
        }

        if (sEntry.getValue().getStorageType() == StorageType.FILESYSTEM) {
            Path entryPath = Path.of(KConfig.getInstance().getProperty("STORAGE_PATH"), key);
            try {
                return Optional.of(new KRecord(key, Files.readAllBytes(entryPath), null, null));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            return  Optional.of(new KRecord(key, sEntry.getValue().getContent(), null, null));
        }
    }
}
