package com.dmitrenko.testcontainertest.model.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ImaggaResponseResultsTagsTag {

    @NotBlank(message = "Field [en] mustn't be empty")
    private String en;
}
