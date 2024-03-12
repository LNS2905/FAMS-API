package com.fams.fams.models.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class FamsApiException extends RuntimeException{
    @Getter
    private final HttpStatus status;

    @Getter
    private final String message;

    public FamsApiException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
