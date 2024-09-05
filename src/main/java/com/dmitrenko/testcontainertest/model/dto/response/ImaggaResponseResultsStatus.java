package com.dmitrenko.testcontainertest.model.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ImaggaResponseResultsStatus {

    private String text;
    private String type;
}
