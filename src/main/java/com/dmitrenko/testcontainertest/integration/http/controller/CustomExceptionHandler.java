package com.dmitrenko.testcontainertest.integration.http.controller;

import com.dmitrenko.testcontainertest.exception.ClientResponseException;
import com.dmitrenko.testcontainertest.exception.ServerResponseException;
import com.dmitrenko.testcontainertest.model.dto.response.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler({EntityNotFoundException.class})
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handleNotFoundException(EntityNotFoundException e) {
        return new ErrorResponse()
            .setCode(String.valueOf(NOT_FOUND.value()))
            .setMessage(e.getMessage());
    }

    @ExceptionHandler({ValidationException.class})
    @ResponseStatus(SERVICE_UNAVAILABLE)
    public ErrorResponse handleValidationException(ValidationException e) {
        return new ErrorResponse()
            .setCode(String.valueOf(SERVICE_UNAVAILABLE.value()))
            .setMessage(e.getMessage());
    }

    @ExceptionHandler({ClientResponseException.class})
    @ResponseStatus(SERVICE_UNAVAILABLE)
    public ErrorResponse handleClientResponseException(ClientResponseException e) {
        return new ErrorResponse()
            .setCode(String.valueOf(SERVICE_UNAVAILABLE.value()))
            .setMessage(e.getBodyErrorResponse());
    }

    @ExceptionHandler({ServerResponseException.class})
    @ResponseStatus(SERVICE_UNAVAILABLE)
    public ErrorResponse handleServerResponseException(ServerResponseException e) {
        return new ErrorResponse()
            .setCode(String.valueOf(SERVICE_UNAVAILABLE.value()))
            .setMessage(e.getBodyErrorResponse());
    }
}
