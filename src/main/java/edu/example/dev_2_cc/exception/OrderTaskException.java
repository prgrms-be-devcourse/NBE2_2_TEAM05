package edu.example.dev_2_cc.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderTaskException extends RuntimeException{

    private String message;
    private int code;

}
