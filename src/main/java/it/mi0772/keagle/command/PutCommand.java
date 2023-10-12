package it.mi0772.keagle.command;

import it.mi0772.keagle.exceptions.KRecordAlreadyExists;
import it.mi0772.keagle.filesystem.Namespace;
import it.mi0772.keagle.receiver.StorageReceiver;
import it.mi0772.keagle.record.KRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;


public class PutCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(PutCommand.class);
    private final StorageReceiver receiver;
    private final String key;
    private final byte[] value;
    private final String namespace;
    private Duration duration;


    public PutCommand(StorageReceiver receiver, String namespace, String key, byte[] value) {
        this(receiver,namespace,  key, value, null);
    }

    public PutCommand(StorageReceiver receiver, String namespace, String key, byte[] value, Duration duration) {
        this.receiver = receiver;
        this.key = key;
        this.value = value;
        this.duration = duration;
        this.namespace = namespace;
    }

    @Override
    public Optional<KRecord> execute() throws KRecordAlreadyExists, IOException {
        new Namespace().getOrCreate(namespace);
        return Optional.ofNullable(receiver.put(this.namespace, this.key, this.value, this.duration));
    }
}
