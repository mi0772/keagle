package it.mi0772.keagle.storage;

import java.util.Optional;

public interface Storage {
    Item insert(String key, byte[] value);
    Optional<Item> find(String key);
}
