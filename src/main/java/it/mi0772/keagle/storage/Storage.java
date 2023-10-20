package it.mi0772.keagle.storage;

import it.mi0772.keagle.exceptions.ItemAlreadyExistException;

import java.io.IOException;
import java.util.Optional;

public interface Storage {
    Item insert(String key, byte[] value) throws ItemAlreadyExistException;
    Optional<Item> find(String key);
}
