package edu.example.dev_2_cc.exception;

public class AccessDeniedException extends org.springframework.security.access.AccessDeniedException {

    public AccessDeniedException(String message) {
        super(message);
    }

    public AccessDeniedException() {
        super(null); // 메시지를 보낼 필요가 없는 경우 사용하는 기본 생성자
    }

}