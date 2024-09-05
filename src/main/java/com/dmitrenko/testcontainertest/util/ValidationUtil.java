package com.dmitrenko.testcontainertest.util;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class ValidationUtil {

    public <D> D validate(@Valid D value) {
        log.info("Successful validation for request: {}", value.getClass());

        return value;
    }
}
