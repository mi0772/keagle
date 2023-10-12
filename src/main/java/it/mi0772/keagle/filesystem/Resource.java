package it.mi0772.keagle.filesystem;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;

public class Resource {

    private final String hash;
    private final String entryPath;
    private Path resourcePath;
    private final String databasePath;
    private final String namespace;

    public Resource(String namespace, String hash) throws IOException {
        Dotenv dotenv = Dotenv.load();
        var ns = new Namespace();
        this.namespace = ns.getOrCreate(namespace);
        this.hash = hash;
        this.entryPath = this.hash;
        this.databasePath = dotenv.get("STORAGE_PATH");
        this.resourcePath = Paths.get(this.databasePath, "records", this.namespace, this.entryPath);

    }

    public boolean exist() {
        return resourcePath.toFile().exists();
    }

    public Path create() throws IOException {
        return Files.createDirectories(resourcePath);
    }

    public boolean store(byte[] content, Duration duration) throws IOException {
        if (!exist())
            create();

        Files.write(resourcePath.resolve("CONTENT"), content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING).toFile().exists();

        if (duration != null) {
            var expire = LocalDateTime.now().plus(duration);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy-HH:mm:ss");
            String timeAsString = expire.format(formatter);
            Files.write(resourcePath.resolve("EXPIRE"), timeAsString.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        }
        return true;
    }

    public byte[] read() throws IOException {
        if (isExpired()) {
            removeExpiredEntry();
            return null;
        }
        return Files.readAllBytes(resourcePath.resolve("CONTENT"));
    }

    public boolean isExpired() throws IOException {
        var expirePath = resourcePath.resolve("EXPIRE");
        if (expirePath.toFile().exists()) {
            var t = Files.readAllBytes(expirePath);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy-HH:mm:ss");
            var expireTime = LocalDateTime.parse(new String(t), formatter);
            if (expireTime.isBefore(LocalDateTime.now())) {
                removeExpiredEntry();
                return true;
            }
        }
        return false;
    }

    private void removeExpiredEntry() throws IOException {
        Path directoryPath = Path.of(this.databasePath,this.entryPath);
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
    }
}
