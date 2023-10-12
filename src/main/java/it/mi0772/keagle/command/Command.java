package it.mi0772.keagle.command;

import it.mi0772.keagle.exceptions.KRecordAlreadyExists;
import it.mi0772.keagle.record.KRecord;

import java.io.IOException;
import java.util.Optional;

public interface Command {
    Optional<KRecord> execute() throws IOException, KRecordAlreadyExists;
}
