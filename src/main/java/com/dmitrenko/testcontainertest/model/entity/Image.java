package com.dmitrenko.testcontainertest.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "objects")
@ToString(callSuper = true, exclude = "objects")
@Table(name = "image")
public class Image extends BaseEntity {

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "image_label")
    private String label;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "image_image_object",
        joinColumns = { @JoinColumn(name = "image_id") },
        inverseJoinColumns = { @JoinColumn(name = "image_object_id") })
    private Set<ImageObject> objects = new HashSet<>();
}
