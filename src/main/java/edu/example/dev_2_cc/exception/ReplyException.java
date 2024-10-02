package edu.example.dev_2_cc.exception;

public enum ReplyException {
    NOT_FOUND("Reply NOT_FOUND", 404),
    NOT_CREATED("Reply Not Created", 400),
    NOT_UPDATED("Reply Not Updated", 400),
    NOT_FETCHED("Reply Not Fetched", 400),
    NOT_DELETED("Reply Not Deleted", 400);

    private ReplyTaskException replyTaskException;

    ReplyException(String message,int code){replyTaskException = new ReplyTaskException(message,code);}

    public ReplyTaskException get(){return replyTaskException;}

}

