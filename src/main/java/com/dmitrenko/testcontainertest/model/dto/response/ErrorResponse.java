package com.dmitrenko.testcontainertest.model.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ErrorResponse {
    private String code;
    private String message;
}
