package com.bol.mancala.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    @ResponseBody
    protected ResponseEntity<Object> handleException(
            RuntimeException ex, WebRequest request) {
        String responseBody = "Ooops! Something went wrong";
        ErrorModel errorModel = ErrorModel.builder()
                .message(responseBody)
                .code("GEN_EX")
                .build();
        return handleExceptionInternal(ex, errorModel, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = {MancalaException.class})
    @ResponseBody
    protected ResponseEntity<Object> handleMancalaApiException(
            RuntimeException ex, WebRequest request) {

        MancalaException mancalaException = (MancalaException) ex;

        ErrorModel errorModel = ErrorModel.builder()
                .message(mancalaException.getMessage())
                .code("MANCALA_EX")
                .build();
        return handleExceptionInternal(ex, errorModel, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    @ResponseBody
    protected ResponseEntity<Object> handleResourceNotFoundException(
            RuntimeException ex, WebRequest request) {

        ResourceNotFoundException mancalaException = (ResourceNotFoundException) ex;

        ErrorModel errorModel = ErrorModel.builder()
                .message(mancalaException.getMessage())
                .code("Resource_Not_FoundException")
                .build();
        return handleExceptionInternal(ex, errorModel, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}
