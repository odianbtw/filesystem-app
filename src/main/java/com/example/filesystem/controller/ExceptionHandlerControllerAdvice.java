package com.example.filesystem.controller;


import com.example.filesystem.api.model.DefaultErrorResponseV1;
import com.example.filesystem.exception.BadRequestException;
import com.example.filesystem.exception.InternalErrorException;
import com.example.filesystem.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Clock;
import java.time.OffsetDateTime;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ExceptionHandlerControllerAdvice {

    private final Clock clock;

    @ExceptionHandler(ResourceNotFoundException.class)
    private ResponseEntity<DefaultErrorResponseV1> resourceNotFoundException(
            ResourceNotFoundException ex
    ) {
        log.error(ex.getMessage(), ex);
        final var response = new DefaultErrorResponseV1();
        response.setMessage(ex.getMessage());
        response.setStatusCode(404);
        response.setTimestamp(OffsetDateTime.now(clock));
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(BadRequestException.class)
    private ResponseEntity<DefaultErrorResponseV1> badRequestException(
            BadRequestException ex
    ) {
        log.error(ex.getMessage(), ex);
        final var response = new DefaultErrorResponseV1();
        response.setMessage(ex.getMessage());
        response.setStatusCode(400);
        response.setTimestamp(OffsetDateTime.now(clock));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(InternalErrorException.class)
    private ResponseEntity<DefaultErrorResponseV1> internalErrorException(
            InternalErrorException ex
    ) {
        log.error(ex.getMessage(), ex);
        final var response = new DefaultErrorResponseV1();
        response.setMessage(ex.getMessage());
        response.setStatusCode(500);
        response.setTimestamp(OffsetDateTime.now(clock));
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }


}
