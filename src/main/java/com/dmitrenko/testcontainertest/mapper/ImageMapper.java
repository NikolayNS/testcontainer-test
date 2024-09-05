package com.dmitrenko.testcontainertest.mapper;

import com.dmitrenko.testcontainertest.model.dto.request.ImageRequest;
import com.dmitrenko.testcontainertest.model.dto.response.ImageResponse;
import com.dmitrenko.testcontainertest.model.entity.Image;

import java.util.Collection;
import java.util.List;

public interface ImageMapper {

    ImageResponse from(Image source);

    default List<ImageResponse> from(Collection<Image> source) {
        return source
            .stream()
            .map(this::from)
            .toList();
    }

    Image from(ImageRequest request);
}
