package com.dmitrenko.testcontainertest.model.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class ImageResponse {

    private UUID id;
    private String url;
    private String label;
    private List<String> objects;
}
