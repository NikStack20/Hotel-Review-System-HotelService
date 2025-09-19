package com.org.Hotel.GlobalExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictHandler extends RuntimeException {
	public ConflictHandler(String msg) {
		super(msg);
	}
}
