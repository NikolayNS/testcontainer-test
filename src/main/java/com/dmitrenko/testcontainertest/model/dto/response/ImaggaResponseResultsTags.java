package com.dmitrenko.testcontainertest.model.dto.response;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ImaggaResponseResultsTags {

    private Double confidence;
    @NotNull(message = "Field [tag] mustn't be null")
    private @Valid ImaggaResponseResultsTagsTag tag;
}
