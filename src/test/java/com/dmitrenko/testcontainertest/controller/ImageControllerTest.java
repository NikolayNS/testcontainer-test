package com.dmitrenko.testcontainertest.controller;

import com.dmitrenko.testcontainertest.config.TestConfig;
import com.dmitrenko.testcontainertest.exception.ClientResponseException;
import com.dmitrenko.testcontainertest.exception.ServerResponseException;
import com.dmitrenko.testcontainertest.integration.http.client.ImaggaClient;
import com.dmitrenko.testcontainertest.model.dto.response.ImageResponse;
import com.dmitrenko.testcontainertest.unit.DataManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

import static com.dmitrenko.testcontainertest.util.Constant.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest(classes = TestConfig.class)
@AutoConfigureMockMvc
class ImageControllerTest {

    @Autowired
    private MockMvc client;
    @Autowired
    private DataManager dataManager;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ImaggaClient imaggaClient;

    @BeforeEach
    public void clean() {
        dataManager.cleanDatabase();
    }

    @Test
    void addPositive() throws Exception {
        var request = dataManager.getFirstImageRequest();
        when(imaggaClient.analyze(request.getUrl())).thenReturn(dataManager.getFistResponse());

        client.perform(post(IMAGES_V1)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.url", is(request.getUrl())))
            .andExpect(jsonPath("$.label", is(request.getLabel())))
            .andExpect(jsonPath("$.objects", hasSize(3)));

        request = dataManager.getSecondImageRequest();
        when(imaggaClient.analyze(request.getUrl())).thenReturn(dataManager.getSecondResponse());

        client.perform(post(IMAGES_V1)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.url", is(request.getUrl())))
            .andExpect(jsonPath("$.label").exists())
            .andExpect(jsonPath("$.objects", hasSize(2)));

        request = dataManager.getThirdImageRequest();

        client.perform(post(IMAGES_V1)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.url", is(request.getUrl())))
            .andExpect(jsonPath("$.label", is(STATIC_LABEL)))
            .andExpect(jsonPath("$.objects").isEmpty());
    }

    @Test
    void getByParamsPositive() throws Exception {
        dataManager.fillUpDatabase();

        client.perform(get(IMAGES_V1))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(6)));

        client.perform(get(IMAGES_V1 + "?objects=dog"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(3)));

        client.perform(get(IMAGES_V1 + "?objects=cat"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getByIdPositive() throws Exception{
        dataManager.fillUpDatabase();
        var images = dataManager.getAll();

        Assertions.assertNotEquals(0, images.size());

        for (ImageResponse image : images) {
            client.perform(get(IMAGE_BY_ID_V1.replace("{imageId}", image.getId().toString())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(image.getId().toString())))
                .andExpect(jsonPath("$.url", is(image.getUrl())))
                .andExpect(jsonPath("$.label", is(image.getLabel())))
                .andExpect(jsonPath("$.objects", hasSize(image.getObjects().size())));
        }
    }

    @Test
    void addValidationException() throws Exception {
        var request = dataManager.getBadImageRequest();

        client.perform(post(IMAGES_V1)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());

        request = dataManager.getFourthImageRequest();
        when(imaggaClient.analyze(request.getUrl())).thenReturn(dataManager.getBadResponse());

        client.perform(post(IMAGES_V1)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isServiceUnavailable())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(String.valueOf(SERVICE_UNAVAILABLE.value()))))
            .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void addImaggaException() throws Exception {
        var request = dataManager.getFourthImageRequest();
        when(imaggaClient.analyze(request.getUrl())).thenThrow(new ClientResponseException(HttpStatusCode.valueOf(503), "Service unavailable"));

        client.perform(post(IMAGES_V1)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isServiceUnavailable())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(String.valueOf(SERVICE_UNAVAILABLE.value()))))
            .andExpect(jsonPath("$.message").exists());

        when(imaggaClient.analyze(anyString())).thenThrow(new ServerResponseException(HttpStatusCode.valueOf(503), "Service unavailable"));

        client.perform(post(IMAGES_V1)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isServiceUnavailable())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(String.valueOf(SERVICE_UNAVAILABLE.value()))))
            .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void getByIdNotFound() throws Exception{
        client.perform(get(IMAGE_BY_ID_V1.replace("{imageId}", UUID.randomUUID().toString())))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$.code", is(String.valueOf(NOT_FOUND.value()))))
            .andExpect(jsonPath("$.message").exists());
    }
}