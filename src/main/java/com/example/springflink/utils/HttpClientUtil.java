package com.example.springflink.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.CompletableFuture;

/**
 * @author wangzuoyu1
 * @description
 */
public class HttpClientUtil {

    public CompletableFuture<String> sendPost(String uri, String data) {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(uri))
            .POST(BodyPublishers.ofString(data))
            .build();
        return client
            .sendAsync(request, BodyHandlers.ofString())
            .thenApply(HttpResponse::body);
    }

    public CompletableFuture<String> sendGet(String uri) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(uri))
            .build();
        return client.sendAsync(request, BodyHandlers.ofString())
            .thenApply(HttpResponse::body);
    }

}
