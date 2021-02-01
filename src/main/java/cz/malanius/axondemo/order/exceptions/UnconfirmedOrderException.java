package cz.malanius.axondemo.order.exceptions;

public class UnconfirmedOrderException extends IllegalStateException {

    public UnconfirmedOrderException() {
        super("Cannot ship an order which has not been confirmed yet.");
    }
}
