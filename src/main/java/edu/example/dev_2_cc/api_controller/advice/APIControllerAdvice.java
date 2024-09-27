package edu.example.dev_2_cc.api_controller.advice;

import edu.example.dev_2_cc.exception.CartTaskException;
import edu.example.dev_2_cc.exception.MemberTaskException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Log4j2
public class APIControllerAdvice {

    // MemberTaskException 예외 처리
    @ExceptionHandler(MemberTaskException.class)
    public ResponseEntity<?> handleMemberTaskException(MemberTaskException e) {

        log.info("--- MemberTaskException 발생 ---");
        log.info("--- e.getClass().getName() : " + e.getClass().getName());
        log.info("--- e.getMessage() : " + e.getMessage());

        // 에러 정보를 담을 HashMap 생성(커스텀 응답 형식)
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error message", e.getMessage());   // 'error' 대신 'error message'로 커스텀
        errorResponse.put("status", e.getCode());             // HTTP 상태 코드
        errorResponse.put("timestamp", LocalDateTime.now());  // 예외 발생 시간 추가

        return ResponseEntity
                .status(e.getCode())   // 예외에서 가져온 상태 코드로 응답 설정
                .body(errorResponse);  // 커스텀 응답 내용을 body에 담아 반환
    }
    @ExceptionHandler(CartTaskException.class)
    public ResponseEntity<?> handleCartException(CartTaskException e) {
        log.info("--- CartTaskException 발생 ---");
        log.info("--- e.getClass().getName() : " + e.getClass().getName());
        log.info("--- e.getMessage() : " + e.getMessage());

        // 에러 정보를 담을 HashMap 생성(커스텀 응답 형식)
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error message", e.getMessage());   // 'error' 대신 'error message'로 커스텀
        errorResponse.put("status", e.getCode());             // HTTP 상태 코드
        errorResponse.put("timestamp", LocalDateTime.now());  // 예외 발생 시간 추가

        return ResponseEntity
                .status(e.getCode())   // 예외에서 가져온 상태 코드로 응답 설정
                .body(errorResponse);  // 커스텀 응답 내용을 body에 담아 반환
    }

    // 유효성 어노테이션 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleArgsException(MethodArgumentNotValidException e) {

        // 에러 정보를 담을 HashMap 생성(커스텀 응답 형식)
        Map<String, Object> errMap = new HashMap<>();

        // 유효성 검사 실패한 필드와 메시지를 맵에 추가
        e.getBindingResult().getFieldErrors()
                .forEach(err -> errMap.put(err.getField(), err.getDefaultMessage()));

        // 에러 발생 시간 추가
        errMap.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(errMap, HttpStatus.BAD_REQUEST);
    }

}



