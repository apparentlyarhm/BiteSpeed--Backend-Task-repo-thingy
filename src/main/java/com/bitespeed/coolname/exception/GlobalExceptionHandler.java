package com.bitespeed.coolname.exception;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.ConnectException;
import java.sql.SQLException;

@RestControllerAdvice
@Hidden // If we dont do this the swagger doc generation fails
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    @ExceptionHandler(RestrictedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    ErrorResponse onBadRequestException(RestrictedException ex) {
        return new ErrorResponse(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler({ConnectException.class}) // The only connection this app makes is the database....
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorResponse handleSqlException(SQLException ex) {
        return new ErrorResponse(69, "Check time!");
    }
}
