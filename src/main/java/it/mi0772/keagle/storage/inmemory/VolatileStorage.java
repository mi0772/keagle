package it.mi0772.keagle.storage.inmemory;

import it.mi0772.keagle.storage.Item;
import it.mi0772.keagle.storage.Storage;

import java.util.Optional;

public class VolatileStorage implements Storage  {

    private final BTree storage;

    private static volatile VolatileStorage instance;
    public static VolatileStorage getInstance() {
        if (instance == null) {
            synchronized (VolatileStorage.class) {
                if (instance == null) {
                    instance = new VolatileStorage();
                }
            }
        }
        return instance;
    }

    private VolatileStorage() {
        this.storage = new BTree();
    }

    public Item insert(String key, byte[] value) {
        var v = new Item(key, value);
        this.storage.insert(v);
        return v;
    }

    public Optional<Item> find(String key) {
        var v = new Item(key);
        return this.storage.search(v);
    }

    class BTreeNode {
        private Item value;
        private BTreeNode left;
        private BTreeNode right;

        public BTreeNode(Item value) {
            this.value = value;
            this.left = null;
            this.right = null;
        }
    }

    class BTree {
        private BTreeNode root;

        public BTree() {
            root = null;
        }

        // Metodo per inserire un elemento nell'albero binario
        public void insert(Item value) {
            root = insertRec(root, value);
        }

        // Metodo di supporto ricorsivo per l'inserimento
        private BTreeNode insertRec(BTreeNode root, Item value) {
            if (root == null) {
                root = new BTreeNode(value);
                return root;
            }

            if (value.getHashedKey().compareTo(root.value.getHashedKey()) < 0) {
                root.left = insertRec(root.left, value);
            } else if (value.getHashedKey().compareTo(root.value.getHashedKey()) > 0) {
                root.right = insertRec(root.right, value);
            }

            return root;
        }

        // Metodo per la ricerca di un elemento nell'albero binario
        public Optional<Item> search(Item value) {
            return searchRec(root, value);
        }

        // Metodo di supporto ricorsivo per la ricerca
        private Optional<Item> searchRec(BTreeNode root, Item value) {
            if (root == null) {
                return Optional.empty();
            }

            if (root.value.getHashedKey().equalsIgnoreCase(value.getHashedKey())) {
                return Optional.of(value);
            }

            if (value.getHashedKey().compareTo(root.value.getHashedKey()) < 0) {
                return searchRec(root.left, value);
            }

            return searchRec(root.right, value);
        }
    }
}