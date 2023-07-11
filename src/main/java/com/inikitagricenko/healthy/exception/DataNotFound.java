package com.inikitagricenko.healthy.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class DataNotFound extends RuntimeException {

	private final HttpStatus status = HttpStatus.NOT_FOUND;

	private final LocalDateTime timestamp = LocalDateTime.now();
	private final int code = status.value();

	public DataNotFound() {
		super("Data not found");
	}

	public DataNotFound(String message) {
		super(message);
	}

	public DataNotFound(String message, Throwable cause) {
		super(message, cause);
	}

	public DataNotFound(Throwable cause) {
		super(cause);
	}

	public DataNotFound(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
