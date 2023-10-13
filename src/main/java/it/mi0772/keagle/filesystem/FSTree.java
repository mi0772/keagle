package it.mi0772.keagle.filesystem;

import java.util.ArrayList;
import java.util.List;

public class FSTree<T extends Comparable> {
    FSNode<T> root;

    public FSTree() {
        this.root = null;
    }
    public void insert(T value) {
        root = insertRecursive(root, value, 0);
    }

    public FSNode<T> find(T target) {
        return findRecursive(this.root, target);
    }

    public List<T> findPath(T target) {
        List<T> percorso = new ArrayList<>();
        findPathRecursive(this.root, target, percorso);
        return percorso;
    }

    private FSNode<T> findRecursive(FSNode<T> nodo, T target) {
        if (nodo == null || nodo.value.equals(target)) {
            return nodo;
        }

        if (target.compareTo(nodo.value) <= 0) {
            return findRecursive(nodo.left, target);
        } else {
            return findRecursive(nodo.right, target);
        }
    }

    private FSNode<T> insertRecursive(FSNode<T> nodo, T value, int livello) {
        if (nodo == null) {
            nodo = new FSNode<>(value);
        } else {
            if (value.compareTo(nodo.value) <= 0 ) {
                nodo.left = insertRecursive(nodo.left, value, (livello + 1) % 2);
            } else {
                nodo.right = insertRecursive(nodo.right, value, (livello + 1) % 2);
            }
        }
        return nodo;
    }

    private boolean findPathRecursive(FSNode<T> nodo, T target, List<T> percorso) {
        if (nodo == null) {
            return false;
        }

        percorso.add(nodo.value);

        if (nodo.value.equals(target)) {
            return true;
        }

        if (target.compareTo(nodo.value) <= 0 && findPathRecursive(nodo.left, target, percorso)) {
            return true;
        }

        if (findPathRecursive(nodo.right, target, percorso)) {
            return true;
        }

        percorso.remove(percorso.size() - 1);
        return false;
    }

}

class FSNode<T extends Comparable> {
    T value;
    FSNode<T> left;
    FSNode<T> right;

    public FSNode(T value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }
}