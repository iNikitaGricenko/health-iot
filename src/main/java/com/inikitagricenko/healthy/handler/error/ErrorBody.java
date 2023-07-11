package com.inikitagricenko.healthy.handler.error;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@Data
public class ErrorBody {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String path;
    private final String message;

    public ErrorBody(HttpStatus httpStatus, WebRequest request, String message) {
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
        this.path = ((ServletWebRequest)request).getRequest().getRequestURI().toString();
        this.message = message;
    }
}
