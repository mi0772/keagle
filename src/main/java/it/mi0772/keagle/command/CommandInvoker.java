package it.mi0772.keagle.command;

import it.mi0772.keagle.exceptions.KRecordAlreadyExists;
import it.mi0772.keagle.record.KRecord;

import java.io.IOException;
import java.util.Optional;

public class CommandInvoker {
    public Optional<KRecord> executeCommand(Command command) throws IOException, KRecordAlreadyExists {
        return command.execute();
    }

}
