package edu.example.dev_2_cc.exception;

public enum CartException {
    NOT_FOUND("NOT_FOUND", 404),
    DUPLICATE("DUPLICATE", 409),
    INVALID("INVALID", 400),
    BAD_CREDENTIALS("BAD_CREDENTIALS", 401);

    private CartTaskException cartTaskException;

    CartException(String message, int code) {
        cartTaskException = new CartTaskException(message, code);
    }

    public CartTaskException get() {
        return cartTaskException;
    }
}
