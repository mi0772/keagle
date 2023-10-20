package it.mi0772.keagle.filesystem;

import it.mi0772.keagle.config.KConfig;
import it.mi0772.keagle.exceptions.ItemAlreadyExistException;
import it.mi0772.keagle.storage.StorageSelector;
import it.mi0772.keagle.types.StorageType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.EnumSet;
import java.util.stream.Stream;

import static org.wildfly.common.Assert.assertNotNull;
import static org.wildfly.common.Assert.assertTrue;

public class PersistentTest {




    @Test
    void BcreateMassive() {

        DeleteDirectory.cancella();
        var t1 = Instant.now();
        var storage = StorageSelector.getStorage(StorageType.PERSITENT);


        Stream.iterate(0,
                        (Integer n) -> n + 1)
                .limit(10_000)
                .forEach(x -> {
                    try {
                        storage.insert("key_"+x, ("value_"+x).getBytes(StandardCharsets.UTF_8));
                    } catch (ItemAlreadyExistException e) {
                        throw new RuntimeException(e);
                    }
                });


        System.out.println("write in "+ ChronoUnit.SECONDS.between(t1, Instant.now()));
        assertNotNull(storage);
        assertTrue(storage.find("key_40").isPresent());

    }

    @Test
    void AreadMassive() {

        var t1 = Instant.now();
        var storage = StorageSelector.getStorage(StorageType.PERSITENT);


        Stream.iterate(0,
                        (Integer n) -> n + 1)
                .limit(10_000)
                .forEach(x -> assertTrue(storage.find("key_"+x).isPresent()));

        var t2 = Instant.now();
        System.out.println("read in "+ ChronoUnit.SECONDS.between(t1, t2));
    }
}

class DeleteDirectory {
    public static void cancella() {
        Path directoryPath = Paths.get(KConfig.getInstance().getStoragePath());

        try {
            // Elimina il contenuto della directory senza cancellare la directory stessa
            Files.walkFileTree(directoryPath, EnumSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    // Gestisci eventuali errori durante la cancellazione del file
                    System.err.println("Errore nella cancellazione del file: " + file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    if (!dir.equals(directoryPath)) {
                        // Evita di cancellare la directory principale
                        Files.delete(dir);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
