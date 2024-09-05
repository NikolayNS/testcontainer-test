package com.dmitrenko.testcontainertest.integration.http.client;

import com.dmitrenko.testcontainertest.model.dto.response.ImaggaResponse;

public interface ImaggaClient {

    ImaggaResponse analyze(String imageUrl);
}
