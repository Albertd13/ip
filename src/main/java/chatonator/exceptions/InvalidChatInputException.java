package chatonator.exceptions;

/**
 * Exception thrown when some invalid input is provided by user
 */
public class InvalidChatInputException extends RuntimeException {
    public InvalidChatInputException(String message) {
        super(message);
    }
}
