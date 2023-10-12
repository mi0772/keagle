package it.mi0772.keagle.filesystem;

import io.github.cdimascio.dotenv.Dotenv;
import it.mi0772.keagle.exceptions.NamespaceAlreadyExist;
import it.mi0772.keagle.exceptions.NamespaceNotFoundException;
import it.mi0772.keagle.hash.MD5Hasher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;

public class Namespace {
    private final String databasePath;
    private final Path namespacePath;

    public Namespace() {

        Dotenv dotenv = Dotenv.load();
        this.databasePath = dotenv.get("STORAGE_PATH");
        this.namespacePath = Path.of(databasePath, "namespaces");

        if (!this.namespacePath.toFile().exists()) {
            try {
                Files.createDirectories(this.namespacePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String getOrCreate(String name) throws IOException {
        try {
            return get(name);
        }
        catch (NamespaceNotFoundException e) {
            return create(name);
        }
    }

    public String createIfAbsent(String name) throws NamespaceAlreadyExist, IOException {
        var nameHash = MD5Hasher.toHex(name);
        var p = this.namespacePath.resolve(nameHash);
        if (p.toFile().exists()) {
            throw new NamespaceAlreadyExist("namespace with name : "+name+" already exist");
        }
        Files.createFile(p);
        Files.writeString(p,name);
        return nameHash;
    }

    public String create(String name) throws IOException {
        var nameHash = MD5Hasher.toHex(name);
        var p = this.namespacePath.resolve(nameHash);
        Files.createFile(p);
        Files.writeString(p,name);
        return nameHash;
    }

    public String get(String name) throws NamespaceNotFoundException {
        var nameHash = MD5Hasher.toHex(name);
        var p = this.namespacePath.resolve(nameHash);
        if (!p.toFile().exists())
            throw new NamespaceNotFoundException("namespace with name : "+name+" does not exists");

        try {
            return Files.readString(p);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
