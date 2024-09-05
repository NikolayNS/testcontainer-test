package com.dmitrenko.testcontainertest.exception;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatusCode;

@Getter
@ToString
public class ClientResponseException extends RuntimeException {
    private final HttpStatusCode httpStatus;
    private final String bodyErrorResponse;

    public ClientResponseException(HttpStatusCode httpStatus, String bodyErrorResponse) {
        this.httpStatus = httpStatus;
        this.bodyErrorResponse = bodyErrorResponse;
    }
}