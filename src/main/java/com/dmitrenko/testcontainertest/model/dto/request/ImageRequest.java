package com.dmitrenko.testcontainertest.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ImageRequest {

    @NotBlank(message = "field [url] mustn't be empty")
    private String url;

    private String label;
    private boolean enableDetection;
}
