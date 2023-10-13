package it.mi0772.keagle.filesystem;

import it.mi0772.keagle.hash.HasherFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FSTreeTest {

    @Test
    void create() {
        FSTree<String> albero = new FSTree<>();

        for (int i=0 ; i < 100 ; i++) {
            var t = HasherFactory.getDefaultHasher().toHex("chiave"+i);
            albero.insert(t);
        }

        assertNotNull(albero);

        var toFind = HasherFactory.getDefaultHasher().toHex("chiave23");
        FSNode<String> r = albero.find(toFind);
        assertNotNull(r);

        List<String> path = albero.findPath(toFind);
        assertNotNull(path);

    }
}