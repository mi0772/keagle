package it.mi0772.keagle.exceptions;

public class ItemAlreadyExistException extends Exception {
    public ItemAlreadyExistException(String message) {
        super(message);
    }

    public ItemAlreadyExistException(Throwable cause) {
        super(cause);
    }
}
