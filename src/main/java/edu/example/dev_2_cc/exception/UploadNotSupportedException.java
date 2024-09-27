package edu.example.dev_2_cc.exception;

public class UploadNotSupportedException extends RuntimeException {
    public UploadNotSupportedException(String message) {
        super(message);
    }
}
