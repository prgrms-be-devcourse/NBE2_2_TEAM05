package edu.example.dev_2_cc.exception;

public enum BoardException {
    NOT_FOUND("NOT_FOUND", 404),
    NOT_REGISTERED("NOT_REGISTERED", 400),
    NOT_MODIFIED("NOT_MODIFIED", 400),
    NOT_REMOVED("NOT_REMOVED", 400),
    DUPLICATE("DUPLICATE", 409),
    INVALID("INVALID", 400),
    BAD_CREDENTIALS("BAD_CREDENTIALS", 401);

    private BoardTaskException boardTaskException;
    BoardException(String message, int code) {boardTaskException = new BoardTaskException(message, code);}
    public BoardTaskException get() {return boardTaskException;}
}
