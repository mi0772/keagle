package it.mi0772.keagle.receiver;

import it.mi0772.keagle.exceptions.KRecordAlreadyExists;
import it.mi0772.keagle.record.KRecord;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;

public interface StorageReceiver {
    KRecord put(String key, byte[] value, Duration duration) throws KRecordAlreadyExists, IOException;
    Optional<KRecord> get(String key) throws IOException;
}
