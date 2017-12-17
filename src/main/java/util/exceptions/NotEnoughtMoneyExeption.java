package util.exceptions;

public class NotEnoughtMoneyExeption extends RuntimeException {
    public NotEnoughtMoneyExeption(Exception e) {
        super(e);
    }
}
