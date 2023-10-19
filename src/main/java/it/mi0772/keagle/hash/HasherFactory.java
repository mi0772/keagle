package it.mi0772.keagle.hash;

import it.mi0772.keagle.config.KConfig;

import java.util.Arrays;

public class HasherFactory {
    private HasherFactory() {
    }

    public enum Type {
        SHA3_244("SHA3"), MD5("MD5");
        private String name;

        Type(String s) {
            this.name = s;
        }

        public String getName() {
            return name;
        }

        static Type fromName(String s) {
            return Arrays.stream(Type.values()).filter(x->x.name.equalsIgnoreCase(s)).findAny().orElseThrow();
        }
    }

    public static Hasher getHasher(Type type) {
        return switch (type) {
            case MD5 -> MD5Hasher.getInstance();
            case SHA3_244 -> SHA3Hasher.getInstance();
        };
    }

    public static Hasher getDefaultHasher() {
        var config = KConfig.getInstance();
        return switch (Type.fromName("SHA3")) {
            case MD5 -> MD5Hasher.getInstance();
            case SHA3_244 -> SHA3Hasher.getInstance();
        };
    }
}
