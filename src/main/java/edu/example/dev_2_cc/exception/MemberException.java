package edu.example.dev_2_cc.exception;

public enum MemberException {
    NOT_FOUND("Member NOT FOUND", 404),
    NOT_REMOVED("Member NOT REMOVED", 400),
    NOT_MODIFIED("Member NOT MODIFIED", 500);


    private final MemberTaskException memberTaskException;

    MemberException(String message, int code) {
        this.memberTaskException = new MemberTaskException(message, code);
    }

    public MemberTaskException get() {
        return memberTaskException;
    }
}
