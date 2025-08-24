package Chatonator.exceptions;

public class InvalidChatInputException extends RuntimeException {
    public InvalidChatInputException(String message) {
        super(message);
    }
}
