package com.dmitrenko.testcontainertest.service.domain.impl;

import com.dmitrenko.testcontainertest.mapper.ImageMapper;
import com.dmitrenko.testcontainertest.model.dto.response.ImageResponse;
import com.dmitrenko.testcontainertest.model.entity.ImageObject;
import com.dmitrenko.testcontainertest.repository.ImageObjectRepository;
import com.dmitrenko.testcontainertest.service.domain.ImageObjectDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageObjectDomainServiceImpl implements ImageObjectDomainService {

    private final ImageObjectRepository imageObjectRepository;

    private final ImageMapper imageMapper;

    @Override
    @Transactional
    public UUID addOrGet(String tag) {
        var object = imageObjectRepository.findByTag(tag)
            .orElse(imageObjectRepository.saveAndFlush(new ImageObject().setTag(tag)));
        return object.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImageResponse> getImageByObjects(List<String> objects) {
        return imageMapper.from(imageObjectRepository
            .findAllByTagIn(objects)
            .stream()
            .map(ImageObject::getImages)
            .flatMap(Set::stream)
            .toList());
    }
}
