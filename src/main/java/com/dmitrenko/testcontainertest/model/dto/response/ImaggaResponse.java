package com.dmitrenko.testcontainertest.model.dto.response;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ImaggaResponse {

    @NotNull(message = "Field [result] mustn't be null")
    private @Valid ImaggaResponseResults result;
    private ImaggaResponseResultsStatus status;
}
