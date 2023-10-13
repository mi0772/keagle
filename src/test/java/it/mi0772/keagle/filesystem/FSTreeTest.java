package it.mi0772.keagle.filesystem;

import it.mi0772.keagle.hash.HasherFactory;
import it.mi0772.keagle.record.KRecord;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FSTreeTest {

    @Test
    void create() {
        Path p = Path.of("C:\\Users\\c.digiuseppe\\progetti\\keagle\\db\\records");
        FSTree albero = FSTree.getInstance();

        assertNotNull(albero);
        /*
        for (int i=0 ; i < 100 ; i++) {
            var k = new KRecord("chiave"+i, "prova".getBytes(StandardCharsets.UTF_8), null, null);
            albero.insert(k);
        }

        assertNotNull(albero);

        var toFind = new KRecord("chiave23", "prova".getBytes(StandardCharsets.UTF_8), null, null);
        FSNode r = albero.find(toFind);
        assertNotNull(r);

        List<String> path = albero.findPath(toFind);
        assertNotNull(path);


         */
    }
}