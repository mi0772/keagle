package it.mi0772.keagle.filesystem;

import it.mi0772.keagle.config.KConfig;
import it.mi0772.keagle.enums.StorageType;

import java.io.File;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class FSTree {

    private static volatile FSTree instance;
    public static FSTree getInstance() {
        if (instance == null) {
            synchronized (FSTree .class) {
                if (instance == null) {
                    instance = new FSTree(Path.of(KConfig.getInstance().getProperty("STORAGE_PATH")));
                }
            }
        }
        return instance;
    }
    FSNode root;

    private FSTree(Path rootDirectory) {
        if (!rootDirectory.toFile().isDirectory())
            throw new IllegalArgumentException("root directory is not a valid directory");

        for (File child : rootDirectory.toFile().listFiles()) {
            if (child.getName().contains("_")) {
                //entry con expire
                var s = child.getName().split("_");
                var t = Long.parseLong(s[1]);
                Instant instant = Instant.ofEpochMilli(t);
                LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();

                var record = new Entry(StorageType.FILESYSTEM, s[0], localDateTime);
                insert(record);
            } else {
                var record = new Entry(StorageType.FILESYSTEM, child.getName());
                insert(record);
            }
        }
    }
    public void insert(Entry value) {
        root = insertRecursive(root, value, 0);
    }

    public FSNode find(String key) {
        return findRecursive(this.root, key);
    }

    public List<String> findPath(Entry target) {
        List<String> percorso = new ArrayList<>();
        findPathRecursive(this.root, target, percorso);
        return percorso;
    }

    private FSNode findRecursive(FSNode nodo, String key) {
        if (nodo == null || nodo.value.getKey().equals(key)) {
            return nodo;
        }

        if (key.compareTo(nodo.value.getKey()) <= 0) {
            return findRecursive(nodo.left, key);
        } else {
            return findRecursive(nodo.right, key);
        }
    }

    private FSNode insertRecursive(FSNode nodo, Entry value, int livello) {
        if (nodo == null) {
            nodo = new FSNode(value);
        } else {
            if (value.getKey().compareTo(nodo.value.getKey()) <= 0 ) {
                nodo.left = insertRecursive(nodo.left, value, (livello + 1) % 2);
            } else {
                nodo.right = insertRecursive(nodo.right, value, (livello + 1) % 2);
            }
        }
        return nodo;
    }

    private boolean findPathRecursive(FSNode nodo, Entry target, List<String> percorso) {
        if (nodo == null) {
            return false;
        }

        percorso.add(nodo.value.getKey());

        if (nodo.value.equals(target)) {
            return true;
        }

        if (target.getKey().compareTo(nodo.value.getKey()) <= 0 && findPathRecursive(nodo.left, target, percorso)) {
            return true;
        }

        if (findPathRecursive(nodo.right, target, percorso)) {
            return true;
        }

        percorso.remove(percorso.size() - 1);
        return false;
    }

}

