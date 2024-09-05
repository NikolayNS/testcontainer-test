package com.dmitrenko.testcontainertest.unit;

import com.dmitrenko.testcontainertest.mapper.ImageMapper;
import com.dmitrenko.testcontainertest.model.dto.request.ImageRequest;
import com.dmitrenko.testcontainertest.model.dto.response.*;
import com.dmitrenko.testcontainertest.model.entity.Image;
import com.dmitrenko.testcontainertest.model.entity.ImageObject;
import com.dmitrenko.testcontainertest.repository.ImageObjectRepository;
import com.dmitrenko.testcontainertest.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.dmitrenko.testcontainertest.util.Constant.STATIC_LABEL;

@Component
@RequiredArgsConstructor
public class DataManager {

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private ImageObjectRepository imageObjectRepository;
    @Autowired
    private ImageMapper imageMapper;

    public ImageRequest getFirstImageRequest() {
        return new ImageRequest()
            .setUrl("https://i.pinimg.com/originals/c8/c8/9e/c8c89e2dda5ee3a23ea1a3b57f331c8c.jpg")
            .setLabel("Cane Corso")
            .setEnableDetection(true);
    }

    public ImageRequest getSecondImageRequest() {
        return new ImageRequest()
            .setUrl("https://t4.ftcdn.net/jpg/00/84/66/63/360_F_84666330_LoeYCZ5LCobNwWePKbykqEfdQOZ6fipq.jpg")
            .setEnableDetection(true);
    }

    public ImageRequest getThirdImageRequest() {
        return new ImageRequest()
            .setUrl("https://t4.ftcdn.net/jpg/00/97/58/97/360_F_97589769_t45CqXyzjz0KXwoBZT9PRaWGHRk5hQqQ.jpg");
    }

    public ImageRequest getFourthImageRequest() {
        return new ImageRequest()
            .setUrl("https://i.pinimg.com/originals/c8/c8/c8c89e2dda5ee3a23ea1a3b57f331c8c.jpg")
            .setLabel("Cane")
            .setEnableDetection(true);
    }

    public ImageRequest getBadImageRequest() {
        return new ImageRequest()
            .setLabel("Cane Corso")
            .setEnableDetection(true);
    }

    public ImaggaResponse getFistResponse() {
        return new ImaggaResponse()
            .setResult(new ImaggaResponseResults()
                .setTags(List.of(
                    new ImaggaResponseResultsTags()
                        .setConfidence(56.01)
                        .setTag(new ImaggaResponseResultsTagsTag()
                            .setEn("dog")),
                    new ImaggaResponseResultsTags()
                        .setConfidence(50.24)
                        .setTag(new ImaggaResponseResultsTagsTag()
                            .setEn("terrier")),
                    new ImaggaResponseResultsTags()
                        .setConfidence(43.27)
                        .setTag(new ImaggaResponseResultsTagsTag()
                            .setEn("hunting dog")),
                    new ImaggaResponseResultsTags()
                        .setConfidence(39.0)
                        .setTag(new ImaggaResponseResultsTagsTag()
                            .setEn("pedigree")),
                    new ImaggaResponseResultsTags()
                        .setConfidence(23.89)
                        .setTag(new ImaggaResponseResultsTagsTag()
                            .setEn("portrait")))));
    }

    public ImaggaResponse getSecondResponse() {
        return new ImaggaResponse()
            .setResult(new ImaggaResponseResults()
                .setTags(List.of(
                    new ImaggaResponseResultsTags()
                        .setConfidence(70.01)
                        .setTag(new ImaggaResponseResultsTagsTag()
                            .setEn("cat")),
                    new ImaggaResponseResultsTags()
                        .setConfidence(55.24)
                        .setTag(new ImaggaResponseResultsTagsTag()
                            .setEn("catty")),
                    new ImaggaResponseResultsTags()
                        .setConfidence(39.27)
                        .setTag(new ImaggaResponseResultsTagsTag()
                            .setEn("house")),
                    new ImaggaResponseResultsTags()
                        .setConfidence(29.0)
                        .setTag(new ImaggaResponseResultsTagsTag()
                            .setEn("pedigree")),
                    new ImaggaResponseResultsTags()
                        .setConfidence(23.89)
                        .setTag(new ImaggaResponseResultsTagsTag()
                            .setEn("portrait")))));
    }

    public ImaggaResponse getBadResponse() {
        return new ImaggaResponse()
            .setResult(new ImaggaResponseResults()
                .setTags(List.of()));
    }

    @Transactional
    public void fillUpDatabase() {
        var tags = List.of("dog", "terrier", "hunting dog", "pedigree", "portrait");
        var ids = tags.stream()
            .map(o -> imageObjectRepository.saveAndFlush(new ImageObject().setTag(o)))
            .map(ImageObject::getId)
            .toList();
        var image = new Image().setUrl("someUrl1").setLabel("caneCorso1");
        image.getObjects().addAll(imageObjectRepository.findByIdIn(ids));
        imageRepository.saveAndFlush(image);

        tags = List.of("pet", "black", "dog", "white", "killer");
        ids = tags.stream()
            .map(o -> imageObjectRepository.saveAndFlush(new ImageObject().setTag(o)))
            .map(ImageObject::getId)
            .toList();
        image = new Image().setUrl("someUrl2").setLabel(STATIC_LABEL);
        image.getObjects().addAll(imageObjectRepository.findByIdIn(ids));
        imageRepository.saveAndFlush(image);

        tags = List.of("pet", "white", "terrier", "dog", "chakki");
        ids = tags.stream()
            .map(o -> imageObjectRepository.saveAndFlush(new ImageObject().setTag(o)))
            .map(ImageObject::getId)
            .toList();
        image = new Image().setUrl("someUrl3").setLabel(STATIC_LABEL);
        image.getObjects().addAll(imageObjectRepository.findByIdIn(ids));
        imageRepository.saveAndFlush(image);

        tags = List.of("cat", "white", "catty", "portrait", "female");
        ids = tags.stream()
            .map(o -> imageObjectRepository.saveAndFlush(new ImageObject().setTag(o)))
            .map(ImageObject::getId)
            .toList();
        image = new Image().setUrl("someUrl4").setLabel(STATIC_LABEL);
        image.getObjects().addAll(imageObjectRepository.findByIdIn(ids));
        imageRepository.saveAndFlush(image);

        tags = List.of("pink", "portrait", "cat");
        ids = tags.stream()
            .map(o -> imageObjectRepository.saveAndFlush(new ImageObject().setTag(o)))
            .map(ImageObject::getId)
            .toList();
        image = new Image().setUrl("someUrl5").setLabel(STATIC_LABEL);
        image.getObjects().addAll(imageObjectRepository.findByIdIn(ids));
        imageRepository.saveAndFlush(image);

        tags = List.of("tree", "green", "mount");
        ids = tags.stream()
            .map(o -> imageObjectRepository.saveAndFlush(new ImageObject().setTag(o)))
            .map(ImageObject::getId)
            .toList();
        image = new Image().setUrl("someUrl6").setLabel(STATIC_LABEL);
        image.getObjects().addAll(imageObjectRepository.findByIdIn(ids));
        imageRepository.saveAndFlush(image);
    }

    @Transactional
    public void cleanDatabase() {
        imageRepository.deleteAll();
        imageObjectRepository.deleteAll();
    }

    @Transactional
    public List<ImageResponse> getAll() {
        return imageMapper.from(imageRepository.findAll());
    }
}
