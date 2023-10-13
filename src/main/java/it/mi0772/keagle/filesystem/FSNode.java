package it.mi0772.keagle.filesystem;

public class FSNode {
    Entry value;
    FSNode left;
    FSNode right;

    public FSNode(Entry value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }

    public Entry getValue() {
        return value;
    }
}