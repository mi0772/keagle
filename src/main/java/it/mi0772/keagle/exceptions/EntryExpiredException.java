package it.mi0772.keagle.exceptions;

public class EntryExpiredException extends Exception {
    public EntryExpiredException(String message) {
        super(message);
    }

    public EntryExpiredException(Throwable cause) {
        super(cause);
    }
}
