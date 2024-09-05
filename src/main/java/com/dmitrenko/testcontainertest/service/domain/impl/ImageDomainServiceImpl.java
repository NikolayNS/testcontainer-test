package com.dmitrenko.testcontainertest.service.domain.impl;

import com.dmitrenko.testcontainertest.mapper.ImageMapper;
import com.dmitrenko.testcontainertest.model.dto.request.ImageRequest;
import com.dmitrenko.testcontainertest.model.dto.response.ImageResponse;
import com.dmitrenko.testcontainertest.model.entity.BaseEntity;
import com.dmitrenko.testcontainertest.model.entity.Image;
import com.dmitrenko.testcontainertest.repository.ImageObjectRepository;
import com.dmitrenko.testcontainertest.repository.ImageRepository;
import com.dmitrenko.testcontainertest.service.domain.ImageDomainService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.dmitrenko.testcontainertest.util.Constant.STATIC_LABEL;

@Service
@RequiredArgsConstructor
public class ImageDomainServiceImpl implements ImageDomainService {

    private final ImageRepository imageRepository;
    private final ImageObjectRepository imageObjectRepository;

    private final ImageMapper imageMapper;

    @Override
    @Transactional(readOnly = true)
    public UUID getByUrl(String url) {
        return imageRepository.findByUrl(url)
            .map(BaseEntity::getId)
            .orElse(null);
    }

    @Override
    @Transactional
    public UUID add(ImageRequest addRequest) {
        var image = imageMapper.from(addRequest);
        image = imageRepository.saveAndFlush(image);
        return image.getId();
    }

    @Override
    @Transactional
    public void update(UUID id, List<UUID> topTags) {
        var image = findById(id);
        var objects = imageObjectRepository.findByIdIn(topTags);
        image.getObjects().addAll(objects);

        if(image.getLabel().equals(STATIC_LABEL) && !objects.isEmpty()) image.setLabel(objects.get(0).getTag());

        imageRepository.saveAndFlush(image);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImageResponse> getAll() {
        return imageMapper.from(imageRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public ImageResponse getById(UUID id) {
        return imageMapper.from(findById(id));
    }

    private Image findById(UUID id) {
        return imageRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(String.format("Entity with id [%s] not found", id)));
    }
}
