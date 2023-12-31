package com.inikitagricenko.healthy.handler;

import com.inikitagricenko.healthy.exception.DataNotFound;
import com.inikitagricenko.healthy.handler.error.ErrorBody;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ValidationException;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DataNotFound.class)
    public ResponseEntity<Object> handleDataNotFound(DataNotFound exception, WebRequest request) {
        return handleException(exception, request, exception.getStatus());
    }

    private ResponseEntity<Object> handleException(Exception exception, WebRequest request, HttpStatus status) {
        HttpHeaders headers = new HttpHeaders();
        ErrorBody body = new ErrorBody(status, request, exception.getMessage());
        return handleExceptionInternal(exception, body, headers, status, request);
    }

}
