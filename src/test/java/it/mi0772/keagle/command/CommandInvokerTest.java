package it.mi0772.keagle.command;

import io.github.cdimascio.dotenv.Dotenv;
import it.mi0772.keagle.exceptions.KRecordAlreadyExists;
import it.mi0772.keagle.receiver.FileSystemStorageReceiver;
import it.mi0772.keagle.receiver.StorageReceiver;
import it.mi0772.keagle.record.KRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.midi.Receiver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;

class CommandInvokerTest {

    private static final Logger logger = LoggerFactory.getLogger(CommandInvokerTest.class);

    @Test
    void test() throws KRecordAlreadyExists, IOException {
        StorageReceiver receiver = new FileSystemStorageReceiver();

        Command putCommand = new PutCommand(receiver, "ciao", "valore".getBytes(StandardCharsets.UTF_8));
        CommandInvoker invoker = new CommandInvoker();

        var result = invoker.executeCommand(putCommand);
        logger.info("put result : {}", result);

        Command getCommand = new GetCommand(receiver, "ciao");
        var value = invoker.executeCommand(getCommand);
        logger.info("get result : {}", value);
        assertTrue(true);
    }

    @Test
    void alreadyExist() throws KRecordAlreadyExists, IOException {
        StorageReceiver receiver = new FileSystemStorageReceiver();

        Command putCommand = new PutCommand(receiver, "ciao", "valore".getBytes(StandardCharsets.UTF_8));
        CommandInvoker invoker = new CommandInvoker();
        assertDoesNotThrow(() -> {
            var result = invoker.executeCommand(putCommand);
        });

        assertThrows(KRecordAlreadyExists.class, () -> {
            var result = invoker.executeCommand(putCommand);
        });

    }

    @BeforeEach
    void before() throws IOException {
        logger.info("cancello database");
        Dotenv dotenv = Dotenv.load();

        Path directoryPath = Paths.get(dotenv.get("STORAGE_PATH"));
        try {
            Files.walkFileTree(directoryPath, EnumSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    // Gestisci eventuali errori
                    System.err.println("Errore durante la cancellazione del file: " + exc.getMessage());
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    if (exc == null) {
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    } else {
                        // Gestisci eventuali errori
                        System.err.println("Errore durante la cancellazione della directory: " + exc.getMessage());
                        return FileVisitResult.CONTINUE;
                    }
                }
            });
            System.out.println("Directory cancellata con successo.");
        } catch (IOException e) {
            System.err.println("Errore durante la cancellazione della directory: " + e.getMessage());
        }

    }

}