package com.dmitrenko.testcontainertest.integration.http.client;

import com.dmitrenko.testcontainertest.exception.ClientResponseException;
import com.dmitrenko.testcontainertest.exception.ServerResponseException;
import com.dmitrenko.testcontainertest.model.dto.response.ImaggaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ImaggaClientImpl implements ImaggaClient{

    @Value("${integration.imagga.base-url}")
    private String baseUrl;

    @Value("${integration.imagga.auth}")
    private String auth;

    @Value("${integration.imagga.tags-url}")
    private String tagsUrl;

    private final WebClient webClient;

    @Override
    public ImaggaResponse analyze(String imageUrl) {
        return get(tagsUrl + "?image_url=" + imageUrl, ImaggaResponse.class);
    }

    private <K> K get(String url, Class<K> response) throws ClientResponseException, ServerResponseException {
        return webClient
            .get()
            .uri(UriComponentsBuilder.fromHttpUrl(baseUrl + url).build().toUri())
            .header("Authorization", "Basic " + auth)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, this::clientExceptionResponse)
            .onStatus(HttpStatusCode::is5xxServerError, this::serverExceptionResponse)
            .bodyToMono(response)
            .block();
    }

    private Mono<? extends Exception> clientExceptionResponse(ClientResponse response) {
        return response
            .bodyToMono(String.class)
            .map(body -> new ClientResponseException(response.statusCode(), body));
    }

    private Mono<? extends Exception> serverExceptionResponse(ClientResponse response) {
        return response
            .bodyToMono(String.class)
            .map(body -> new ServerResponseException(response.statusCode(), body));
    }
}
