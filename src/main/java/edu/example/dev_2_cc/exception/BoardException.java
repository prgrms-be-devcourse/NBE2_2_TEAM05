package edu.example.dev_2_cc.exception;

public enum BoardException {
    NOT_FOUND("Board NOT_FOUND", 404),
    NOT_CREATED("Board Not Created", 400),
    NOT_UPDATED("Board Not Updated", 400),
    NOT_DELETED("Board Not Deleted", 400),
    //    NOT_FETCHED("Board Not Fetched", 400),
    IMAGE_NOT_FOUND("No Board Image", 400);

    private BoardTaskException boardTaskException;

    BoardException(String message, int code){
        boardTaskException = new BoardTaskException(message, code);
    }

    public BoardTaskException get(){
        return boardTaskException;
    }


}
