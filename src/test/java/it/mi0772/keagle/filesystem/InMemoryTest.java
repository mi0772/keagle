package it.mi0772.keagle.filesystem;

import it.mi0772.keagle.exceptions.ItemAlreadyExistException;
import it.mi0772.keagle.storage.StorageSelector;
import it.mi0772.keagle.types.StorageType;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.wildfly.common.Assert.assertNotNull;
import static org.wildfly.common.Assert.assertTrue;

class InMemoryTest {

    @Test
    void create() throws ItemAlreadyExistException {

        var storage = StorageSelector.getStorage(StorageType.VOLATILE);

        storage.insert("carlo", "di giuseppe".getBytes(StandardCharsets.UTF_8));
        storage.insert("andrea", "di giuseppe".getBytes(StandardCharsets.UTF_8));
        storage.insert("vittoria", "di giuseppe".getBytes(StandardCharsets.UTF_8));
        storage.insert("angela", "giannella".getBytes(StandardCharsets.UTF_8));

        assertNotNull(storage);

    }

    @Test
    void createMassive() {

        var storage = StorageSelector.getStorage(StorageType.VOLATILE);


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


        assertNotNull(storage);
        assertTrue(storage.find("key_40").isPresent());

    }
}