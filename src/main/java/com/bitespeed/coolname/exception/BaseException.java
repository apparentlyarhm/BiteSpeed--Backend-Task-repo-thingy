package com.bitespeed.coolname.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseException extends Exception {

    private Integer code;
    private String message;

    public BaseException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
