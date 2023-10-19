package it.mi0772.keagle.storage.persistent;

import it.mi0772.keagle.storage.Item;
import it.mi0772.keagle.storage.Storage;
import it.mi0772.keagle.storage.inmemory.VolatileStorage;

import java.util.Optional;

public class PersistentStorage implements Storage {
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

    @Override
    public Item insert(String key, byte[] value) {
        return null;
    }

    @Override
    public Optional<Item> find(String key) {
        return Optional.empty();
    }
}
