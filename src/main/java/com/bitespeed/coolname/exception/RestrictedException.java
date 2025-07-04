package com.bitespeed.coolname.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestrictedException extends BaseException {

    public RestrictedException(Integer code, String message) {
        super(code, message);
    }
}
