package it.mi0772.keagle.command;

import io.github.cdimascio.dotenv.Dotenv;
import it.mi0772.keagle.enums.StorageType;
import it.mi0772.keagle.exceptions.EntryExpiredException;
import it.mi0772.keagle.exceptions.KRecordAlreadyExists;
import it.mi0772.keagle.exceptions.NamespaceNotFoundException;
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
import java.rmi.server.UID;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.EnumSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CommandInvokerTest {

    private static final Logger logger = LoggerFactory.getLogger(CommandInvokerTest.class);

    @Test
    void testFS() throws KRecordAlreadyExists, IOException, NamespaceNotFoundException, EntryExpiredException {
        StorageReceiver receiver = new FileSystemStorageReceiver();

        var key = UUID.randomUUID().toString();

        Command putCommand = new PutCommand(receiver,key, "valore".getBytes(StandardCharsets.UTF_8), StorageType.FILESYSTEM);
        CommandInvoker invoker = new CommandInvoker();

        var result = invoker.executeCommand(putCommand);
        logger.info("put result : {}", result);

        Command getCommand = new GetCommand(receiver,key);
        var value = invoker.executeCommand(getCommand);
        logger.info("get result : {}", value);
        assertTrue(true);
    }

    @Test
    void testMemory() throws KRecordAlreadyExists, IOException, NamespaceNotFoundException, EntryExpiredException {
        StorageReceiver receiver = new FileSystemStorageReceiver();
        var key = UUID.randomUUID().toString();

        Command putCommand = new PutCommand(receiver, key, "valore".getBytes(StandardCharsets.UTF_8), StorageType.MEMORY);
        CommandInvoker invoker = new CommandInvoker();

        var result = invoker.executeCommand(putCommand);
        logger.info("put result : {}", result);

        Command getCommand = new GetCommand(receiver,key);
        var value = invoker.executeCommand(getCommand);
        logger.info("get result : {}", value);
        assertTrue(true);
    }

    @Test
    void testExpired() throws KRecordAlreadyExists, IOException, NamespaceNotFoundException, InterruptedException, EntryExpiredException {
        StorageReceiver receiver = new FileSystemStorageReceiver();
        var key = UUID.randomUUID().toString();

        Command putCommand = new PutCommand(receiver, key, "valore".getBytes(StandardCharsets.UTF_8), Duration.ofSeconds(10), StorageType.MEMORY);
        CommandInvoker invoker = new CommandInvoker();

        var result = invoker.executeCommand(putCommand);
        logger.info("put result : {}", result);

        Command getCommand = new GetCommand(receiver,key);
        var value = invoker.executeCommand(getCommand);
        logger.info("get result : {}", value);
        assertTrue(true);

        Thread.sleep(11_000);


        assertThrows(EntryExpiredException.class, () -> {
            var v = invoker.executeCommand(getCommand);
            logger.info("get result : {}", v);
        });

    }

    @Test
    void alreadyExist() throws KRecordAlreadyExists, IOException {
        StorageReceiver receiver = new FileSystemStorageReceiver();

        Command putCommand = new PutCommand(receiver,  "ciao", "valore".getBytes(StandardCharsets.UTF_8), StorageType.FILESYSTEM);
        CommandInvoker invoker = new CommandInvoker();
        assertDoesNotThrow(() -> {
            var result = invoker.executeCommand(putCommand);
        });

        assertThrows(KRecordAlreadyExists.class, () -> {
            var result = invoker.executeCommand(putCommand);
        });
    }

    //@BeforeEach
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