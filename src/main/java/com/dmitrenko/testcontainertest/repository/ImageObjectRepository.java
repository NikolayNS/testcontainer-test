package com.dmitrenko.testcontainertest.repository;

import com.dmitrenko.testcontainertest.model.entity.ImageObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ImageObjectRepository extends JpaRepository<ImageObject, UUID> {

    List<ImageObject> findAllByTagIn(List<String> tags);

    Optional<ImageObject> findByTag(String tag);

    List<ImageObject> findByIdIn(List<UUID> ids);
}
