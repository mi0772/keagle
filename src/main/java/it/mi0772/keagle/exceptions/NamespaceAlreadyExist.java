package it.mi0772.keagle.exceptions;

public class NamespaceAlreadyExist extends Exception {
    public NamespaceAlreadyExist(String message) {
        super(message);
    }

    public NamespaceAlreadyExist(Throwable cause) {
        super(cause);
    }
}
