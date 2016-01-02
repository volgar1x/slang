package slang;

/**
 * @author Antoine Chauvin
 */
public class SExpression extends RuntimeException {

    public SExpression() {
    }

    public SExpression(String message) {
        super(message);
    }

    public SExpression(String message, Throwable cause) {
        super(message, cause);
    }

    public SExpression(Throwable cause) {
        super(cause);
    }

    public SExpression(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
