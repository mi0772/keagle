package it.mi0772.keagle.storage;

import it.mi0772.keagle.storage.inmemory.VolatileStorage;
import it.mi0772.keagle.storage.persistent.PersistentStorage;
import it.mi0772.keagle.types.StorageType;

public class StorageSelector {
    public static Storage getStorage(StorageType type) {
        switch (type) {
            case VOLATILE: return VolatileStorage.getInstance();
            case PERSITENT: return PersistentStorage.getInstance();
            default: throw new IllegalArgumentException("Storage tyoe not found");
        }
    }
}
