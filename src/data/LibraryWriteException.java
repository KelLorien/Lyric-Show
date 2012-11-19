package data;

/**
 * User: jpipe
 * Date: 11/19/12
 */
public class LibraryWriteException extends Exception {

    public LibraryWriteException(String message) {
        super(message);
    }

    public LibraryWriteException(String message, Throwable cause) {
        super(message, cause);
    }
}
