package it.mi0772.keagle.exceptions;

public class NamespaceNotFoundException extends Exception {
    public NamespaceNotFoundException(String message) {
        super(message);
    }

    public NamespaceNotFoundException(Throwable cause) {
        super(cause);
    }
}
