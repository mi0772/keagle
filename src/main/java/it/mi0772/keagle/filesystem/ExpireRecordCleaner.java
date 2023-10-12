package it.mi0772.keagle.filesystem;

import io.github.cdimascio.dotenv.Dotenv;

public class ExpireRecordCleaner {

    //TODO: Implementare classe
    private String databasePath;

    public ExpireRecordCleaner() {
        Dotenv dotenv = Dotenv.load();
        this.databasePath = dotenv.get("STORAGE_PATH");
    }

    public void clean() {

    }


}
