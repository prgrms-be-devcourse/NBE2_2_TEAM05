package edu.example.dev_2_cc.exception;

public enum MemberException {

    NOT_FOUND("NOT_FOUND", 404),
    DUPLICATE("DUPLICATE(중복되는 아이디가 존재합니다)", 409), // 임의로 에러 메시지 변경,
    INVALID("INVALID", 400),
    BAD_CREDENTIALS("BAD_CREDENTIALS", 401),
    NOT_MODIFIED("NOT_MODIFIED", 405),
    NOT_REMOVED("NOT_REMOVED", 402 );

    private MemberTaskException memberTaskException;

    MemberException(String message, int code) {
        memberTaskException = new MemberTaskException(message, code);
    }

    public MemberTaskException get(){
        return memberTaskException;
    }

}
