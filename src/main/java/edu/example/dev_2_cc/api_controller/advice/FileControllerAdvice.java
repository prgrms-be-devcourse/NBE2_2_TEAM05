package edu.example.dev_2_cc.api_controller.advice;

import edu.example.dev_2_cc.exception.UploadNotSupportedException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Log4j2
public class FileControllerAdvice {

    // 파일 크기 초과 예외 처리
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> maxUploadSizeExceeded(MaxUploadSizeExceededException e) {

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "파일 크기 제한 초과: " + e.getMessage());
        errorResponse.put("timestamp", LocalDateTime.now());

        return ResponseEntity.badRequest().body(errorResponse);
    }

    // 지원되지 않는 업로드 예외 처리
    @ExceptionHandler(UploadNotSupportedException.class)
    public ResponseEntity<?> uploadNotSupportedException(UploadNotSupportedException e) {

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", e.getMessage());
        errorResponse.put("timestamp", LocalDateTime.now());

        return ResponseEntity.badRequest().body(errorResponse);
    }

}