package it.mi0772.keagle.command;

import it.mi0772.keagle.exceptions.EntryExpiredException;
import it.mi0772.keagle.receiver.StorageReceiver;
import it.mi0772.keagle.record.KRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class GetCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(PutCommand.class);

    private final StorageReceiver receiver;
    private final String key;


    public GetCommand(StorageReceiver receiver, String key) {
        this.receiver = receiver;
        this.key = key;
    }

    @Override
    public Optional<KRecord> execute() throws EntryExpiredException {
        return receiver.get(this.key);

    }
}
