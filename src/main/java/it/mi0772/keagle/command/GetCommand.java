package it.mi0772.keagle.command;

import it.mi0772.keagle.exceptions.NamespaceNotFoundException;
import it.mi0772.keagle.filesystem.Namespace;
import it.mi0772.keagle.receiver.StorageReceiver;
import it.mi0772.keagle.record.KRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class GetCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(PutCommand.class);

    private final StorageReceiver receiver;
    private final String key;
    private final String namespace;

    public GetCommand(StorageReceiver receiver, String namespace, String key) {
        this.receiver = receiver;
        this.key = key;
        this.namespace = namespace;
    }

    @Override
    public Optional<KRecord> execute() throws NamespaceNotFoundException {
        new Namespace().get(this.namespace);
        return receiver.get(this.namespace, this.key);

    }
}
