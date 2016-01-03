package slang;

/**
 * @author Antoine Chauvin
 */
public class SException extends RuntimeException {

    public SException() {
    }

    public SException(String message) {
        super(message);
    }

    public SException(String message, Throwable cause) {
        super(message, cause);
    }

    public SException(Throwable cause) {
        super(cause);
    }

    public SException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
