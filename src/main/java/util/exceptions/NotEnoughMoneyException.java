package util.exceptions;

public class NotEnoughMoneyException extends RuntimeException {
    public NotEnoughMoneyException() {
        super();
    }

    public NotEnoughMoneyException(RuntimeException e) {
        super(e);
    }
}
