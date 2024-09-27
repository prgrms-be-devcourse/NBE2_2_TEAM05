package edu.example.dev_2_cc.exception;

public class UploadNotSupportedException extends RuntimeException {
    public UploadNotSupportedException(String message) {


        // 부모 클래스의(RuntimeException) 생성자를 호출하여 메시지 설정
        super(message);
    }

}

