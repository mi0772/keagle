package it.mi0772.keagle.record;

import it.mi0772.keagle.hash.HasherFactory;
import it.mi0772.keagle.hash.MD5Hasher;
import org.junit.jupiter.api.Test;

class KRecordTest {

    @Test
    void testHash() {
        System.out.println(HasherFactory.getDefaultHasher().toHex("ciao"));
        System.out.println(HasherFactory.getDefaultHasher().calculate("ciao"));
    }

}
