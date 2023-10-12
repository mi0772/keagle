package it.mi0772.keagle.record;

import it.mi0772.keagle.hash.MD5Hasher;
import org.junit.jupiter.api.Test;

class KRecordTest {

    @Test
    void testHash() {
        System.out.println(MD5Hasher.toHex("ciao"));
        System.out.println(MD5Hasher.toB64("ciao"));
        System.out.println(MD5Hasher.calculate("ciao"));
    }

}
