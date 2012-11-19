package data;

/**
 * User: jpipe
 * Date: 11/19/12
 */
public class LibraryReadException extends Exception {

    public LibraryReadException(String message) {
        super(message);
    }

    public LibraryReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
