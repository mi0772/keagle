package it.mi0772.keagle.config;

import io.github.cdimascio.dotenv.Dotenv;

public class KConfig {
    private final Dotenv dotenv;

    private static volatile KConfig instance;
    public static KConfig getInstance() {
        if (instance == null) {
            synchronized (KConfig .class) {
                if (instance == null) {
                    instance = new KConfig();
                }
            }
        }
        return instance;
    }

    private KConfig() {
        this.dotenv = Dotenv.load();
    }

    public String getHttpServerPort() {
        return this.dotenv.get("SERVER_PORT");
    }

    public String getHttpServerAddress() {
        return this.dotenv.get("SERVER_ADDRESS");
    }

    public String getStoragePath() {
        return this.dotenv.get("STORAGE_PATH");
    }
}
