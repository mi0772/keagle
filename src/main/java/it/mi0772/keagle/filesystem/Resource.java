package it.mi0772.keagle.filesystem;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

public class Resource {

    private final String hash;
    private final List<String> path;
    private Path resourcePath;
    private final String databasePath;

    public Resource(String hash) {
        Dotenv dotenv = Dotenv.load();

        this.hash = hash;
        this.path = Arrays.stream(splitHashToPaths(this.hash)).toList();
        this.databasePath = dotenv.get("STORAGE_PATH");
        this.resourcePath = Paths.get(this.databasePath);
        this.path.forEach(p -> this.resourcePath = this.resourcePath.resolve(p));
    }

    public boolean exist() {
        return resourcePath.toFile().exists();
    }

    public Path create() throws IOException {
        return Files.createDirectories(resourcePath);
    }

    public boolean store(Path p, byte[] content) throws IOException {
        return Files.write(resourcePath.resolve("CONTENT"), content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING).toFile().exists();
    }

    public byte[] read() throws IOException {
        return Files.readAllBytes(resourcePath.resolve("CONTENT"));
    }

    private String[] splitHashToPaths(String hash) {
        if (hash.length() != 32) {
            throw new IllegalArgumentException("L'hash deve essere di 32 caratteri (256 bit).");
        }

        String[] pathComponents = new String[4];
        for (int i = 0; i < 4; i++) {
            int start = i * 8;
            int end = start + 8;
            pathComponents[i] = hash.substring(start, end);
        }

        return pathComponents;
    }
}
