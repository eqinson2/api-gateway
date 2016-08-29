package gateway.core.exception;

/**
 * Created by snow_young on 16/3/23.
 */
public class QuitException extends RuntimeException {
    public QuitException(String message) {
        super(message);
    }

    public QuitException(String message, Exception cause) {
        super(message, cause);
    }

    public QuitException(Exception cause) {
        super(cause);
    }

}
