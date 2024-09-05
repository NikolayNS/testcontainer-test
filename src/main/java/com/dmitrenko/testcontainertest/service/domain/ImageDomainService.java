package com.dmitrenko.testcontainertest.service.domain;

import com.dmitrenko.testcontainertest.model.dto.request.ImageRequest;
import com.dmitrenko.testcontainertest.model.dto.response.ImageResponse;

import java.util.List;
import java.util.UUID;

public interface ImageDomainService {

    UUID getByUrl(String url);

    UUID add(ImageRequest addRequest);

    void update(UUID id, List<UUID> topTags);

    List<ImageResponse> getAll();

    ImageResponse getById(UUID id);
}
