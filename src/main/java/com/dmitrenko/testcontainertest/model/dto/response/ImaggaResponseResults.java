package com.dmitrenko.testcontainertest.model.dto.response;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

@Data
@Accessors(chain = true)
public class ImaggaResponseResults {

    @NotEmpty(message = "Field [en] mustn't be empty")
    private List<@Valid ImaggaResponseResultsTags> tags = new LinkedList<>();
}
