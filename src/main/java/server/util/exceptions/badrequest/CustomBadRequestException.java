package server.util.exceptions.badrequest;

public abstract class CustomBadRequestException extends RuntimeException {
    public CustomBadRequestException(String message) {
        super(message);
    }
}
