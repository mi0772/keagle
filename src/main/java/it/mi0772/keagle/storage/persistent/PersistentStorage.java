package it.mi0772.keagle.storage.persistent;

import it.mi0772.keagle.config.KConfig;
import it.mi0772.keagle.exceptions.ItemAlreadyExistException;
import it.mi0772.keagle.storage.Item;
import it.mi0772.keagle.storage.Storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class PersistentStorage implements Storage {
    private final PStorage storage;

    private static volatile PersistentStorage instance;
    public static PersistentStorage getInstance() {
        if (instance == null) {
            synchronized (PersistentStorage.class) {
                if (instance == null) {
                    instance = new PersistentStorage();
                }
            }
        }
        return instance;
    }

    private PersistentStorage() {
        this.storage = new PStorage(Path.of(KConfig.getInstance().getStoragePath()));
    }

    @Override
    public Item insert(String key, byte[] value) throws ItemAlreadyExistException {
        var v = new Item(key, value);
        try {
            if (this.storage.insert(v))
                return v;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        throw new IllegalStateException("cannot insert "+v);
    }

    @Override
    public Optional<Item> find(String key) {
        try {
            return this.storage.get(key);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    class PStorage {
        private final Path entryPath;

        public PStorage(Path entryPath) {
            this.entryPath = entryPath;
            if (!entryPath.toFile().isDirectory()) {
                throw new IllegalStateException("Entry path : "+entryPath.toString() + " is not a directory");
            }
        }

        public boolean insert(Item item) throws ItemAlreadyExistException, IOException {
            if (!isPresent(item)) {
                Path itemPath = this.entryPath.resolve(Path.of(item.getHashedKey()));
                Files.createDirectories(itemPath);
                Files.write(itemPath.resolve(Path.of(item.getHashedKey())), item.getValue().orElse("".getBytes()));
                return true;
            }
            throw new ItemAlreadyExistException("item with key "+item.getKey()+ " already exists");
        }

        public boolean isPresent(Item item)  {
            Path itemPathDir = this.entryPath.resolve(Path.of(item.getHashedKey()));
            if (!itemPathDir.toFile().isDirectory())
                return false;

            var itemPath = itemPathDir.resolve(Path.of(item.getHashedKey()));
            if (!itemPath.toFile().isFile()) {
                return false;
            }

            return true;
        }

        public boolean isPresent(String key) {
            return this.isPresent(new Item(key));
        }

        public Optional<Item> get(String key) throws IOException {
            if (!isPresent(key))
                return Optional.empty();

            var item = new Item(key);
            var itemPath = this.entryPath.resolve(Path.of(item.getHashedKey(),item.getHashedKey()));

            var value = Files.readAllBytes(itemPath);
            return Optional.of(new Item(key, value));
        }

    }
}
