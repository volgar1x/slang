package slang.expressions;

/**
 * @author Antoine Chauvin
 */
public class SlangException extends RuntimeException {
    public SlangException() {
    }

    public SlangException(String message) {
        super(message);
    }

    public SlangException(String message, Throwable cause) {
        super(message, cause);
    }

    public SlangException(Throwable cause) {
        super(cause);
    }

    public SlangException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
